package com.avanade.decolatech.viajava.unit.services.usuarios;

import com.avanade.decolatech.viajava.domain.dtos.request.user.CreateUserRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.user.CreateUserResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.mapper.UserMapper;
import com.avanade.decolatech.viajava.domain.model.Document;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.model.enums.DocumentType;
import com.avanade.decolatech.viajava.domain.model.enums.UserRole;
import com.avanade.decolatech.viajava.domain.repository.DocumentRepository;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.service.user.CreateUserService;
import com.avanade.decolatech.viajava.utils.UserExceptionMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private CreateUserService createUserService;

    private CreateUserRequest request;

    private User user;

    private Document document;

    @BeforeEach
    void setup() {
        request = CreateUserRequest
                .builder()
                .name("Josué")
                .email("josue@email.com")
                .password("josuedogera")
                .phone("11983627505")
                .documentNumber("00000000000")
                .documentType(DocumentType.CPF.name())
                .build();

        user = User
                .builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phone(request.getPhone())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        document = Document
                .builder()
                .documentNumber(request.getDocumentNumber())
                .user(user)
                .documentType(DocumentType.valueOf(request.getDocumentType()))
                .build();
    }

    @Test
    @DisplayName("createUsuario() deve criar usuário e documento quando dados são válidos")
    void createUsuario_DeveCriarUsuarioEDocumento_QuandoDadosSaoValidos() {
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
        given(userRepository.findByPhone(request.getPhone())).willReturn(Optional.empty());
        given(documentRepository.findById(request.getDocumentNumber())).willReturn(Optional.empty());
        given(userMapper.toUser(request)).willReturn(user);
        given(userRepository.save(user)).willReturn(user);
        given(documentRepository.save(any(Document.class))).willReturn(document);
        given(userMapper.toCreateUsuarioResponse(user, document.getDocumentNumber()))
                .willReturn(new CreateUserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        document.getDocumentNumber()
                ));

        CreateUserResponse response = createUserService.createUser(request, UserRole.CLIENT);

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getPhone(), response.phone());
        assertEquals(document.getDocumentNumber(), response.documentNumber());
    }

    @Test
    @DisplayName("createUsuario() deve lançar BusinessException quando e-mail já existe")
    void createUsuario_DeveLancarBusinessException_QuandoEmailJaExiste() {
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(user));

        BusinessException exception = assertThrows(BusinessException.class, () -> createUserService.createUser(request, UserRole.CLIENT));
        assertTrue(exception.getMessage().contains(UserExceptionMessages.USER_EMAIL_ALREADY_EXISTS));
        verify(userRepository, never()).save(any(User.class));
        verify(documentRepository, never()).save(any(Document.class));
    }

    @Test
    @DisplayName("createUsuario() deve lançar BusinessException quando phone já existe")
    void createUsuario_DeveLancarBusinessException_QuandoTelefoneJaExiste() {
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
        given(userRepository.findByPhone(request.getPhone())).willReturn(Optional.of(user));

        BusinessException exception = assertThrows(BusinessException.class, () -> createUserService.createUser(request, UserRole.CLIENT));
        assertTrue(exception.getMessage().contains(UserExceptionMessages.USER_PHONE_ALREADY_EXISTS));
        verify(userRepository, never()).save(any(User.class));
        verify(documentRepository, never()).save(any(Document.class));
    }

    @Test
    @DisplayName("createUsuario() deve lançar BusinessException quando documento já existe")
    void createUsuario_DeveLancarBusinessException_QuandoDocumentoJaExiste() {
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
        given(userRepository.findByPhone(request.getPhone())).willReturn(Optional.empty());
        given(documentRepository.findById(request.getDocumentNumber())).willReturn(Optional.of(document));

        BusinessException exception = assertThrows(BusinessException.class, () -> createUserService.createUser(request, UserRole.CLIENT));
        assertTrue(exception.getMessage().contains(UserExceptionMessages.USER_DOCUMENT_ALREADY_EXISTS));
        verify(userRepository, never()).save(any(User.class));
        verify(documentRepository, never()).save(any(Document.class));
    }

}