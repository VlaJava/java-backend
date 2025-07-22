package com.avanade.decolatech.viajava.service.usuario;

import com.avanade.decolatech.viajava.domain.dtos.request.ResetarSenhaRequest;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.exception.LinkValidationException;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import com.avanade.decolatech.viajava.service.email.EmailService;
import com.avanade.decolatech.viajava.strategy.EmailType;
import com.avanade.decolatech.viajava.utils.UsuarioExceptionMessages;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ForgotPasswordService {

    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final JwtDecoder jwtDecoder;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordService(UsuarioRepository usuarioRepository, EmailService emailService, JwtDecoder jwtDecoder, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.jwtDecoder = jwtDecoder;
        this.passwordEncoder = passwordEncoder;
    }

    public void enviarEmailRecuperarSenha(String email) {
        this.usuarioRepository.findByEmail(email).ifPresentOrElse(usuario -> {
                    this.emailService.enviarEmail(usuario, EmailType.PASSWORD_RESET_EMAIL);
                },

                () -> {
                    throw new ResourceNotFoundException(UsuarioExceptionMessages.USUARIO_NAO_EXISTE);
                });
    }

    public void alterarSenha(ResetarSenhaRequest request, String token) {
        String novaSenha = request.getNovaSenha();
        String novaSenhaConfirmacao = request.getNovaSenhaConfirmacao();

        if (!novaSenha.equals(novaSenhaConfirmacao)) {
            throw new BusinessException(String.format("[%s alterarSenha] -  As senhas devem ser equivalentes.", ForgotPasswordService.class.getName()));
        }

        Jwt tokenDecodificado = this.jwtDecoder.decode(token);
        String email = tokenDecodificado.getSubject();
        String purpose = tokenDecodificado.getClaimAsString("purpose");
        Instant expiracao = tokenDecodificado.getExpiresAt();

        if (expiracao != null && expiracao.isBefore(Instant.now())) {
            throw new LinkValidationException(String.format("[%s alterarSenha] -  O link de confirmação expirou.", ForgotPasswordService.class.getName()));
        }

        if (!purpose.equals("password_reset")) {
            throw new LinkValidationException(String.format("[%s alterarSenha] -  Token Inválido para esta operação.", ForgotPasswordService.class.getName()));
        }

        this.usuarioRepository.findByEmail(email).ifPresentOrElse(usuario -> {
            String novaSenhaHash = this.passwordEncoder.encode(novaSenha);
            usuario.setSenha(novaSenhaHash);

            this.usuarioRepository.save(usuario);
        }, () -> {
            throw new ResourceNotFoundException(UsuarioExceptionMessages.USUARIO_NAO_EXISTE);
        });


    }
}
