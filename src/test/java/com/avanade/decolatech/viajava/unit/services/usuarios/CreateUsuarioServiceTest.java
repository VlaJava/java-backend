package com.avanade.decolatech.viajava.unit.services.usuarios;

import com.avanade.decolatech.viajava.domain.dtos.request.CreateUsuarioRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUsuarioResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.mapper.UsuarioMapper;
import com.avanade.decolatech.viajava.domain.model.Documento;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.model.enums.TipoDocumento;
import com.avanade.decolatech.viajava.domain.repository.DocumentoRepository;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import com.avanade.decolatech.viajava.service.usuario.CreateUsuarioService;
import com.avanade.decolatech.viajava.utils.UsuarioExceptionMessages;
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
class CreateUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private DocumentoRepository documentoRepository;

    @InjectMocks
    private CreateUsuarioService createUsuarioService;

    private CreateUsuarioRequest request;

    private Usuario usuario;

    private Documento documento;

    @BeforeEach
    void setup() {
        request = CreateUsuarioRequest
                .builder()
                .nome("Josué")
                .email("josue@email.com")
                .senha("josuedogera")
                .telefone("11983627505")
                .numeroDocumento("00000000000")
                .tipoDocumento(TipoDocumento.CPF)
                .build();

        usuario = Usuario
                .builder()
                .id(UUID.randomUUID())
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha())
                .telefone(request.getTelefone())
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build();

        documento = Documento
                .builder()
                .numeroDocumento(request.getNumeroDocumento())
                .usuario(usuario)
                .tipoDocumento(request.getTipoDocumento())
                .build();
    }

    @Test
    @DisplayName("createUsuario() deve criar usuário e documento quando dados são válidos")
    void createUsuario_DeveCriarUsuarioEDocumento_QuandoDadosSaoValidos() {
        given(usuarioRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
        given(usuarioRepository.findByTelefone(request.getTelefone())).willReturn(Optional.empty());
        given(documentoRepository.findById(request.getNumeroDocumento())).willReturn(Optional.empty());
        given(usuarioMapper.toUsuario(request)).willReturn(usuario);
        given(usuarioRepository.save(usuario)).willReturn(usuario);
        given(documentoRepository.save(any(Documento.class))).willReturn(documento);
        given(usuarioMapper.toCreateUsuarioResponse(usuario, documento.getNumeroDocumento()))
                .willReturn(new CreateUsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTelefone(),
                        documento.getNumeroDocumento()
                ));

        CreateUsuarioResponse response = createUsuarioService.criarUsuario(request);

        assertNotNull(response);
        assertEquals(usuario.getId(), response.id());
        assertEquals(usuario.getNome(), response.nome());
        assertEquals(usuario.getEmail(), response.email());
        assertEquals(usuario.getTelefone(), response.telefone());
        assertEquals(documento.getNumeroDocumento(), response.numeroDocumento());
    }

    @Test
    @DisplayName("createUsuario() deve lançar BusinessException quando e-mail já existe")
    void createUsuario_DeveLancarBusinessException_QuandoEmailJaExiste() {
        given(usuarioRepository.findByEmail(request.getEmail())).willReturn(Optional.of(usuario));

        BusinessException exception = assertThrows(BusinessException.class, () -> createUsuarioService.criarUsuario(request));
        assertTrue(exception.getMessage().contains(UsuarioExceptionMessages.USUARIO_EMAIL_JA_EXISTE));
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(documentoRepository, never()).save(any(Documento.class));
    }

    @Test
    @DisplayName("createUsuario() deve lançar BusinessException quando telefone já existe")
    void createUsuario_DeveLancarBusinessException_QuandoTelefoneJaExiste() {
        given(usuarioRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
        given(usuarioRepository.findByTelefone(request.getTelefone())).willReturn(Optional.of(usuario));

        BusinessException exception = assertThrows(BusinessException.class, () -> createUsuarioService.criarUsuario(request));
        assertTrue(exception.getMessage().contains(UsuarioExceptionMessages.USUARIO_TELEFONE_JA_EXISTE));
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(documentoRepository, never()).save(any(Documento.class));
    }

    @Test
    @DisplayName("createUsuario() deve lançar BusinessException quando documento já existe")
    void createUsuario_DeveLancarBusinessException_QuandoDocumentoJaExiste() {
        given(usuarioRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
        given(usuarioRepository.findByTelefone(request.getTelefone())).willReturn(Optional.empty());
        given(documentoRepository.findById(request.getNumeroDocumento())).willReturn(Optional.of(documento));

        BusinessException exception = assertThrows(BusinessException.class, () -> createUsuarioService.criarUsuario(request));
        assertTrue(exception.getMessage().contains(UsuarioExceptionMessages.USUARIO_DOCUMENTO_JA_EXISTE));
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(documentoRepository, never()).save(any(Documento.class));
    }

}