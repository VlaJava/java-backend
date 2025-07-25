package com.avanade.decolatech.viajava.service.usuario;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import com.avanade.decolatech.viajava.utils.UsuarioExceptionMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ReativarUsuarioContaService {

    private final UsuarioRepository usuarioRepository;

    public ReativarUsuarioContaService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void execute(UUID id) {
        User user = usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("[%s execute] - %s",
                                        ReativarUsuarioContaService.class.getName(),
                                        UsuarioExceptionMessages.USUARIO_NAO_EXISTE)
                        ));

        if(user.isAtivo()) {
            return;
        }

        user.setAtivo(true);

        usuarioRepository.save(user);
    }
}
