package com.avanade.decolatech.viajava.service.usuario;

import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetAllUsuariosService {

    private final UsuarioRepository usuarioRepository;

    public GetAllUsuariosService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Page<Usuario> execute(Integer page, Integer size) {
        return usuarioRepository
                .findAll(PageRequest.of(page, size));
    }
}
