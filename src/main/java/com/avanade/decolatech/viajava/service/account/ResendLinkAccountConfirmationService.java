package com.avanade.decolatech.viajava.service.conta;

import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import com.avanade.decolatech.viajava.service.email.EmailService;
import com.avanade.decolatech.viajava.strategy.EmailType;
import com.avanade.decolatech.viajava.utils.UsuarioExceptionMessages;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ReenviarLinkConfirmacaoContaService {

    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    public ReenviarLinkConfirmacaoContaService(UsuarioRepository usuarioRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    @Transactional(readOnly = true)
    public void execute(String email) {
        Usuario usuario = this.usuarioRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(UsuarioExceptionMessages.USUARIO_NAO_EXISTE));

        if (usuario.isAtivo()) {
            throw new BusinessException(
                    String.format("[%s enviarEmail] - A conta em questão já está ativada.]",
                            ReenviarLinkConfirmacaoContaService.class.getName()
                    ));
        }

        this.emailService.enviarEmail(usuario, EmailType.RESEND_ACCOUNT_CONFIRMATION_EMAIL);

    }
}
