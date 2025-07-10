package com.avanade.decolatech.viajava.unit.controllers.usuarios;

import com.avanade.decolatech.viajava.controller.UsuarioController;
import com.avanade.decolatech.viajava.domain.dtos.request.CreateUsuarioRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUsuarioResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.UsuarioResponse;
import com.avanade.decolatech.viajava.domain.mapper.UsuarioMapper;
import com.avanade.decolatech.viajava.domain.model.Documento;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.model.enums.TipoDocumento;
import com.avanade.decolatech.viajava.domain.repository.DocumentoRepository;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import com.avanade.decolatech.viajava.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@WebMvcTest(controllers = UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private DocumentoRepository documentoRepository;

    @MockitoBean
    private CreateUsuarioService createUsuarioService;

    @MockitoBean
    private GetAllUsuariosService getAllUsuariosService;

    @MockitoBean
    private GetUsuarioByIdService getUsuarioByIdService;

    @MockitoBean
    private ReativarUsuarioContaService reativarUsuarioContaService;

    @MockitoBean
    private DeleteUsuarioService deleteUsuarioService;

    @MockitoBean
    private UsuarioMapper usuarioMapper;

    Usuario usuario;

    CreateUsuarioRequest createUsuarioRequest;

    Documento documento;

    @BeforeEach
    void setup() {
        createUsuarioRequest = CreateUsuarioRequest
                .builder()
                .nome("José Marciel")
                .email("josemarciel002@email.com")
                .senha("Senha123463")
                .telefone("2199557712")
                .numeroDocumento("12345678901")
                .tipoDocumento(TipoDocumento.CPF)
                .build();

        usuario = Usuario
                .builder()
                .id(UUID.randomUUID())
                .nome(createUsuarioRequest.getNome())
                .email(createUsuarioRequest.getEmail())
                .senha(createUsuarioRequest.getSenha())
                .telefone(createUsuarioRequest.getTelefone())
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build();

        documento = Documento
                .builder()
                .numeroDocumento(createUsuarioRequest.getNumeroDocumento())
                .tipoDocumento(TipoDocumento.CPF)
                .usuario(usuario)
                .build();
    }

    @DisplayName("Dado um objeto usuario quando for criar o usuário deve retornar usuario salvo")
    @Test
    void testDadoObjetoUsuario_QuandoForCriarOUsuario_DeveRetornarUsuarioSalvo() throws Exception {
        CreateUsuarioResponse response = new CreateUsuarioResponse(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTelefone(), createUsuarioRequest.getNumeroDocumento()
        );

        given(this.createUsuarioService.criarUsuario(any(CreateUsuarioRequest.class))).willReturn(response);
        given(this.usuarioMapper.toCreateUsuarioResponse(any(Usuario.class), any(String.class))).willReturn(response);

        ResultActions resultActions = this.mockMvc
                .perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUsuarioRequest)));

        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(response.id().toString())))
                .andExpect(jsonPath("$.nome", is(response.nome())))
                .andExpect(jsonPath("$.email", is(response.email())))
                .andExpect(jsonPath("$.telefone", is(response.telefone())))
                .andExpect(jsonPath("$.numeroDocumento", is(response.numeroDocumento())));
    }
}