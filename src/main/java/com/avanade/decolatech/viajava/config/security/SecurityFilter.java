package com.avanade.decolatech.viajava.config.security;

import com.avanade.decolatech.viajava.domain.exception.ApplicationException;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
    private final UserRepository repository;
    private final AuthService authService;

    public SecurityFilter(UserRepository userRepository, AuthService authService) {
        this.repository = userRepository;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.readToken(request);

        try {
            if (token != null) {
                var login = this.authService.validateToken(token);

                UserDetails user = this.repository.findByUsername(login);

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        catch (NullPointerException e) {
            logger.error("[SecurityFilter] - Token is null {} ", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ApplicationException applicationException = new ApplicationException();
            applicationException.setPath(request.getRequestURI());
            applicationException.setMethod(request.getMethod());
            applicationException.setCode(HttpStatus.UNAUTHORIZED.value());
            applicationException.setStatus(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            applicationException.setMessage("Invalid token");
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(applicationException));

        }
        filterChain.doFilter(request, response);
    }

    private String readToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
