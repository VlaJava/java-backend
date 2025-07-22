package com.avanade.decolatech.viajava.service.usuario;

import com.avanade.decolatech.viajava.domain.dtos.request.CreateUsuarioRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUsuarioResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.mapper.UsuarioMapper;
import com.avanade.decolatech.viajava.domain.model.Documento;
import com.avanade.decolatech.viajava.domain.model.Role;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.model.enums.TipoDocumento;
import com.avanade.decolatech.viajava.domain.model.enums.UsuarioRole;
import com.avanade.decolatech.viajava.domain.repository.DocumentoRepository;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;

import com.avanade.decolatech.viajava.service.email.EmailService;
import com.avanade.decolatech.viajava.strategy.EmailType;

import com.avanade.decolatech.viajava.utils.UsuarioExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import java.time.Period;
import java.util.Optional;


@Service
public class CreateUsuarioService {

    private final Logger logger = LoggerFactory.getLogger(CreateUsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final DocumentoRepository documentoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final EmailService emailService;

    public CreateUsuarioService(
            UsuarioRepository usuarioRepository,
            DocumentoRepository documentoRepository, PasswordEncoder passwordEncoder,
            UsuarioMapper usuarioMapper, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.documentoRepository = documentoRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.emailService = emailService;
    }

    @Transactional
    public CreateUsuarioResponse criarUsuario(CreateUsuarioRequest request, UsuarioRole usuarioRole) {
        this.validateIfDataExists(request);
        this.validateBirthDayDate(request.getDataNasc());

        Usuario usuarioToSave = usuarioMapper.toUsuario(request);

        usuarioToSave
                .setSenha(this.passwordEncoder.encode(usuarioToSave.getSenha()));

        usuarioToSave.setAtivo(false);

        Role role =  Role
                .builder()
                .usuarioRole(usuarioRole)
                .usuario(usuarioToSave)
                .build();
        usuarioToSave.setRole(role);

        Usuario usuario = usuarioRepository.save(usuarioToSave);

       Documento documento = this.salvarDocumento(request, usuario);

       this.emailService.enviarEmail(usuario, EmailType.ACCOUNT_CREATED_EMAIL);

       return usuarioMapper.toCreateUsuarioResponse(usuario, documento.getNumeroDocumento());
    }


    public void validateBirthDayDate(LocalDate dataNascimento) {
        if(Period.between(dataNascimento, LocalDate.now()).getYears() < 18 ){
            throw new BusinessException(String.format("[%s validateBirthdayDate] - " +
                            "Usuário deve ter 18 anos ou mais."
            , CreateUsuarioService.class.getName()));
        }
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
                documentoRepository.findByNumeroDocumento(request.getNumeroDocumento());

        if(documentoJaExiste.isPresent()) {
            throw new BusinessException(
                    String.format("[%s validateIfDataExists] - %s",
                            CreateUsuarioService.class.getName(),
                            UsuarioExceptionMessages.USUARIO_DOCUMENTO_JA_EXISTE)
            );
        }
    }

    @Transactional
    public Documento salvarDocumento(CreateUsuarioRequest request, Usuario usuario) {
        TipoDocumento tipoDocumento = TipoDocumento.valueOf(request.getTipoDocumento());
        this.validateTipoDocumento(tipoDocumento, request.getNumeroDocumento());

        Documento documento = Documento
                .builder()
                .numeroDocumento(request.getNumeroDocumento())
                .usuario(usuario)
                .tipoDocumento(tipoDocumento)
                .build();

        return this.documentoRepository.save(documento);
    }

    public void validateTipoDocumento(TipoDocumento documento, String numeroDocumento) {
        if(documento.equals(TipoDocumento.CPF) && !numeroDocumento.matches("^[0-9]{11}$")) {
            throw new BusinessException("CPF Inválido.");
        }

        if(documento.equals(TipoDocumento.PASSAPORTE) && !numeroDocumento.matches("^[0-9]{8}$"))  {
            throw new BusinessException("Pasaporte inválido.");
        }
    }
}
