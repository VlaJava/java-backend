package com.avanade.decolatech.viajava.service.conta;


import com.avanade.decolatech.viajava.domain.exception.LinkValidationException;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import com.avanade.decolatech.viajava.service.email.EmailService;
import com.avanade.decolatech.viajava.strategy.EmailType;
import com.avanade.decolatech.viajava.strategy.factory.EmailFactory;
import com.avanade.decolatech.viajava.utils.UsuarioExceptionMessages;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AccountConfirmationService {

    private final UsuarioRepository usuarioRepository;
    private final JwtDecoder jwtDecoder;
    private final EmailService emailService;

    public AccountConfirmationService(UsuarioRepository usuarioRepository, JwtDecoder jwtDecoder, EmailFactory emailFactory, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtDecoder = jwtDecoder;
        this.emailService = emailService;
    }

    @Transactional
    public void confirmarConta(String token) {
        Jwt extractedToken = this.jwtDecoder.decode(token);
        String email = extractedToken.getSubject();
        Instant expiration = extractedToken.getExpiresAt();
        String purpose = extractedToken.getClaimAsString("purpose");

        if (expiration != null && expiration.isBefore(Instant.now())) {
            throw new LinkValidationException(String.format("[%s confirmarConta] -  O link de confirmação expirou.", AccountConfirmationService.class.getName()));
        }

        if (!purpose.equals("account_activation")) {
            throw new LinkValidationException(String.format("[%s confirmarConta] - O token é inválido para esta operação.", AccountConfirmationService.class.getName()));
        }

        this.usuarioRepository.findByEmail(email).ifPresentOrElse(usuario -> {
            usuario.setAtivo(true);
            this.usuarioRepository.save(usuario);

            this.emailService.enviarEmail(usuario, EmailType.ACCOUNT_CONFIRMATION_EMAIL);
        }, () -> {
            throw new ResourceNotFoundException(UsuarioExceptionMessages.USUARIO_NAO_EXISTE);
        });
    }
}
