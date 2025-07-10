package com.avanade.decolatech.viajava.service;

import com.avanade.decolatech.viajava.domain.dtos.request.CreateUsuarioRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUsuarioResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.mapper.UsuarioMapper;
import com.avanade.decolatech.viajava.domain.model.Documento;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.repository.DocumentoRepository;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;

import com.avanade.decolatech.viajava.utils.UsuarioExceptionMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CreateUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final DocumentoRepository documentoRepository;
    private final UsuarioMapper usuarioMapper;

    public CreateUsuarioService(
            UsuarioRepository usuarioRepository,
            DocumentoRepository documentoRepository,
            UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.documentoRepository = documentoRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public CreateUsuarioResponse criarUsuario(CreateUsuarioRequest request) {
        this.validateIfDataExists(request);

        Usuario usuarioToSave = usuarioMapper.toUsuario(request);

        Usuario usuario = usuarioRepository.save(usuarioToSave);

       Documento documento = this.salvarDocumento(request, usuario);

       return usuarioMapper.toCreateUsuarioResponse(usuario, documento.getNumeroDocumento());
    }

    public void validateIfDataExists(CreateUsuarioRequest request) {
        Optional<Usuario> usuarioEmailExists =
                usuarioRepository.findByEmail(request.getEmail());
        if(usuarioEmailExists.isPresent()) {
            throw new BusinessException(
                    String.format("[%s validateIfDataExists] - %s",
                            CreateUsuarioService.class.getName(),
                            UsuarioExceptionMessages.USUARIO_EMAIL_JA_EXISTE)
            );
        }

        Optional<Usuario> usuarioTelefoneExists =
                usuarioRepository.findByTelefone(request.getTelefone());


        if(usuarioTelefoneExists.isPresent()) {
            throw new BusinessException(
                    String.format("[%s validateIfDataExists] - %s",
                            CreateUsuarioService.class.getName(),
                            UsuarioExceptionMessages.USUARIO_TELEFONE_JA_EXISTE)
            );
        }

        Optional<Documento> documentoJaExiste =
                documentoRepository.findById(request.getNumeroDocumento());

        if(documentoJaExiste.isPresent()) {
            throw new BusinessException(
                    String.format("[%s validateIfDataExists] - %s",
                            CreateUsuarioService.class.getName(),
                            UsuarioExceptionMessages.USUARIO_DOCUMENTO_JA_EXISTE)
            );
        }
    }

    public Documento salvarDocumento(CreateUsuarioRequest request, Usuario usuario) {
        Documento documento = new Documento(request.getNumeroDocumento(), usuario, request.getTipoDocumento());

        documentoRepository.save(documento);

        return documento;
    }

}
