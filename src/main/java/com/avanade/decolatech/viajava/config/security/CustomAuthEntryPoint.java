package com.avanade.decolatech.viajava.config.security;


import com.avanade.decolatech.viajava.domain.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public CustomAuthEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse res,
                         AuthenticationException ex) throws IOException {
        ApplicationException err = new ApplicationException();
        err.setPath(req.getRequestURI());
        err.setMethod(req.getMethod());
        err.setCode(HttpStatus.UNAUTHORIZED.value());
        err.setStatus(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        err.setMessage("Credenciais invalidas");
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(mapper.writeValueAsString(err));
    }
}
