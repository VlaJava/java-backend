package com.avanade.decolatech.viajava.unit.services.usuarios;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import com.avanade.decolatech.viajava.service.usuario.ReativarUsuarioContaService;
import com.avanade.decolatech.viajava.utils.UsuarioExceptionMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReativarUsuarioContaServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReativarUsuarioContaService reativarUsuarioContaService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
      this.usuario =  Usuario.builder()
                .id(UUID.randomUUID())
                .nome("Lucas")
                .email("lucas@email.com")
                .senha("lucaspass")
                .telefone("21992477513")
                .ativo(false)
                .dataCadastro(LocalDateTime.now())
                .build();
    }

    @Test
    void reativarUsuario_DeveAtualizarUsuarioComAtivoTrue_QuandoUsuarioExistir() {
        boolean expectedStatus = true;
        given(usuarioRepository.findById(usuario.getId())).willReturn(Optional.of(usuario));

        reativarUsuarioContaService.execute(usuario.getId());

        assertEquals(expectedStatus, usuario.isAtivo());
    }

    @Test
    void reativarUsuario_DeveNaoFazerNada_QuandoUsuarioJaEstiverAtivo() {
        given(usuarioRepository.findById(usuario.getId())).willReturn(Optional.of(usuario));

        usuario.setAtivo(true);

        reativarUsuarioContaService.execute(usuario.getId());

        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    void reativarUsuario_DeveDispararResourceNotFoundException_QuandoUsuarioNaoExistir() {
        given(usuarioRepository.findById(usuario.getId())).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            reativarUsuarioContaService.execute(usuario.getId());
        });

        assertNotNull(exception);
        assertInstanceOf(ResourceNotFoundException.class, exception);
        assertTrue(exception.getMessage().contains(UsuarioExceptionMessages.USUARIO_NAO_EXISTE));
        verify(usuarioRepository, never()).save(usuario);
    }
}