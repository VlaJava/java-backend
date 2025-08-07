package com.avanade.decolatech.viajava.service.auth;

import com.avanade.decolatech.viajava.domain.dtos.response.user.LoginResponse;

import com.avanade.decolatech.viajava.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public LoginResponse generateToken(User user) {
        var now = Instant.now();

        String roleName = user.getRole().getUserRole().name();

        var claims = JwtClaimsSet.builder()
                .issuer("viajava-backend")
                .subject(user.getId().toString())
                .expiresAt(now.plusSeconds(3 * 60 * 60))
                .claim("scope", roleName)
                .claim("username", user.getEmail())
                .build();

        var token = jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();

        return new LoginResponse(token, 3L);
    }

    public String validateToken(String token) {
        try {
            return this.jwtDecoder.decode(token).getClaimAsString("username");
        } catch (JwtException e) {
            this.logger.error("[AuthService validateToken] - Falha ao validar token: {}", e.getMessage());

            return null;
        }

    }

}
