package com.avanade.decolatech.viajava.service.usuario;

import com.avanade.decolatech.viajava.domain.dtos.request.CreateUserRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUserResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.mapper.UsuarioMapper;
import com.avanade.decolatech.viajava.domain.model.Document;
import com.avanade.decolatech.viajava.domain.model.Role;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.model.enums.DocumentType;
import com.avanade.decolatech.viajava.domain.model.enums.UserRole;
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
    public CreateUserResponse criarUsuario(CreateUserRequest request, UserRole userRole) {
        this.validateIfDataExists(request);
        this.validateBirthDayDate(request.getDataNasc());

        User userToSave = usuarioMapper.toUsuario(request);

        userToSave
                .setSenha(this.passwordEncoder.encode(userToSave.getSenha()));

        userToSave.setAtivo(false);

        Role role =  Role
                .builder()
                .userRole(userRole)
                .user(userToSave)
                .build();
        userToSave.setRole(role);

        User user = usuarioRepository.save(userToSave);

       Document document = this.salvarDocumento(request, user);

       this.emailService.enviarEmail(user, EmailType.ACCOUNT_CREATED_EMAIL);

       return usuarioMapper.toCreateUsuarioResponse(user, document.getNumeroDocumento());
    }


    public void validateBirthDayDate(LocalDate dataNascimento) {
        if(Period.between(dataNascimento, LocalDate.now()).getYears() < 18 ){
            throw new BusinessException(String.format("[%s validateBirthdayDate] - " +
                            "Usuário deve ter 18 anos ou mais."
            , CreateUsuarioService.class.getName()));
        }
    }

    public void validateIfDataExists(CreateUserRequest request) {
        Optional<User> usuarioEmailExists =
                usuarioRepository.findByEmail(request.getEmail());
        if(usuarioEmailExists.isPresent()) {
            throw new BusinessException(
                    String.format("[%s validateIfDataExists] - %s",
                            CreateUsuarioService.class.getName(),
                            UsuarioExceptionMessages.USUARIO_EMAIL_JA_EXISTE)
            );
        }

        Optional<User> usuarioTelefoneExists =
                usuarioRepository.findByTelefone(request.getTelefone());


        if(usuarioTelefoneExists.isPresent()) {
            throw new BusinessException(
                    String.format("[%s validateIfDataExists] - %s",
                            CreateUsuarioService.class.getName(),
                            UsuarioExceptionMessages.USUARIO_TELEFONE_JA_EXISTE)
            );
        }

        Optional<Document> documentoJaExiste =
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
    public Document salvarDocumento(CreateUserRequest request, User user) {
        this.validateTipoDocumento(DocumentType.valueOf(request.getTipoDocumento()), request.getNumeroDocumento());

        Document document = Document
                .builder()
                .numeroDocumento(request.getNumeroDocumento())
                .user(user)
                .tipoDocumento(DocumentType.valueOf(request.getTipoDocumento()))
                .build();

        return this.documentoRepository.save(document);
    }

    public void validateTipoDocumento(DocumentType documento, String numeroDocumento) {
        if(documento.equals(DocumentType.CPF) && !numeroDocumento.matches("^[0-9]{11}$")) {
            throw new BusinessException("CPF Inválido.");
        }

        if(documento.equals(DocumentType.PASSAPORTE) && !numeroDocumento.matches("^[0-9]{8}$"))  {
            throw new BusinessException("Pasaporte inválido.");
        }
    }
}
