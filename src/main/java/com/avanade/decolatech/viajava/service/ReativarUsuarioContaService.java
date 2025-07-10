package com.avanade.decolatech.viajava.service;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Usuario;
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
        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("[%s execute] - %s",
                                        ReativarUsuarioContaService.class.getName(),
                                        UsuarioExceptionMessages.USUARIO_NAO_EXISTE)
                        ));

        if(usuario.isAtivo()) {
            return;
        }

        usuario.setAtivo(true);

        usuarioRepository.save(usuario);
    }
}
