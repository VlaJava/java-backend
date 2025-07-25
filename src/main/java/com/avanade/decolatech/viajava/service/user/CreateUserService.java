package com.avanade.decolatech.viajava.service.user;

import com.avanade.decolatech.viajava.domain.dtos.request.CreateUserRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUserResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.mapper.UserMapper;
import com.avanade.decolatech.viajava.domain.model.Document;
import com.avanade.decolatech.viajava.domain.model.Role;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.model.enums.DocumentType;
import com.avanade.decolatech.viajava.domain.model.enums.UserRole;
import com.avanade.decolatech.viajava.domain.repository.DocumentRepository;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;

import com.avanade.decolatech.viajava.service.email.EmailService;
import com.avanade.decolatech.viajava.strategy.EmailType;

import com.avanade.decolatech.viajava.utils.UserExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import java.time.Period;
import java.util.Optional;


@Service
public class CreateUserService {

    private final Logger logger = LoggerFactory.getLogger(CreateUserService.class);
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;

    public CreateUserService(
            UserRepository userRepository,
            DocumentRepository documentRepository, PasswordEncoder passwordEncoder,
            UserMapper userMapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request, UserRole userRole) {
        this.validateIfDataExists(request);
        this.validateBirthDayDate(request.getBirthdate());

        User userToSave = userMapper.toUser(request);

        userToSave
                .setPassword(this.passwordEncoder.encode(userToSave.getPassword()));

        userToSave.setActive(false);

        Role role =  Role
                .builder()
                .userRole(userRole)
                .user(userToSave)
                .build();
        userToSave.setRole(role);

        User user = userRepository.save(userToSave);

       Document document = this.saveDocument(request, user);

       this.emailService.sendEmail(user, EmailType.ACCOUNT_CREATED_EMAIL);

       return userMapper.toCreateUsuarioResponse(user, document.getDocumentNumber());
    }


    public void validateBirthDayDate(LocalDate dataNascimento) {
        if(Period.between(dataNascimento, LocalDate.now()).getYears() < 18 ){
            throw new BusinessException(String.format("[%s validateBirthdayDate] - " +
                            "User must have at least 18 years or more."
            , CreateUserService.class.getName()));
        }
    }

    public void validateIfDataExists(CreateUserRequest request) {
        Optional<User> userEmailExists =
                userRepository.findByEmail(request.getEmail());
        if(userEmailExists.isPresent()) {
            throw new BusinessException(
                    String.format("[%s validateIfDataExists] - %s",
                            CreateUserService.class.getName(),
                            UserExceptionMessages.USER_EMAIL_ALREADY_EXISTS)
            );
        }

        Optional<User> userPhoneExists =
                userRepository.findByPhone(request.getPhone());


        if(userPhoneExists.isPresent()) {
            throw new BusinessException(
                    String.format("[%s validateIfDataExists] - %s",
                            CreateUserService.class.getName(),
                            UserExceptionMessages.USER_PHONE_ALREADY_EXISTS)
            );
        }
    }

    @Transactional
    public Document saveDocument(CreateUserRequest request, User user) {
        this.validateDocumentType(DocumentType.valueOf(request.getDocumentType()), request.getDocumentNumber());

        Document document = Document
                .builder()
                .documentNumber(request.getDocumentNumber())
                .user(user)
                .documentType(DocumentType.valueOf(request.getDocumentType()))
                .build();

        return this.documentRepository.save(document);
    }

    public void validateDocumentType(DocumentType document, String documentNumber) {
        if(document.equals(DocumentType.CPF) && !documentNumber.matches("^[0-9]{11}$")) {
            throw new BusinessException("Invalid CPF.");
        }

        if(document.equals(DocumentType.PASSPORT) && !documentNumber.matches("^[0-9]{8}$"))  {
            throw new BusinessException("Invalid Passport.");
        }
    }
}
