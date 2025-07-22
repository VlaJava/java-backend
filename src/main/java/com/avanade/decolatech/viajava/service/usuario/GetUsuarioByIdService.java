package com.avanade.decolatech.viajava.service.usuario;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import com.avanade.decolatech.viajava.utils.UsuarioExceptionMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetUsuarioByIdService {

    private final UsuarioRepository usuarioRepository;

    public GetUsuarioByIdService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Usuario execute(UUID id) {
        return usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("[%s execute] - %s",
                                        GetUsuarioByIdService.class.getName(),
                                        UsuarioExceptionMessages.USUARIO_NAO_EXISTE)
                        ));
    }
}
