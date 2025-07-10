package com.avanade.decolatech.viajava.unit.services.usuarios;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import com.avanade.decolatech.viajava.service.DeleteUsuarioService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class DeleteUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DeleteUsuarioService deleteUsuarioService;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = Usuario
                .builder()
                .id(UUID.randomUUID())
                .nome("Maria")
                .email("maria@email.com")
                .senha("mary00534")
                .telefone("21992477508")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build();
    }

    @Test
    void deleteUsuario_DeveAtualizarUsuarioComAtivoFalse_QuandoUsuarioExistir() {
        boolean expectedStatus = false;
        given(usuarioRepository.findById(usuario.getId())).willReturn(Optional.of(usuario));

        deleteUsuarioService.execute(usuario.getId());

        assertEquals(expectedStatus, usuario.isAtivo());

    }

    @Test
    void deleteUsuario_DeveNaoFazerNada_QuandoUsuarioJaEstiverInativo() {
        given(usuarioRepository.findById(usuario.getId())).willReturn(Optional.of(usuario));

        usuario.setAtivo(false);

        deleteUsuarioService.execute(usuario.getId());

        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    void deleteUsuario_DeveDispararResourceNotFoundException_QuandoUsuarioNaoExistir() {
        given(usuarioRepository.findById(usuario.getId())).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deleteUsuarioService.execute(usuario.getId());
        });

        assertNotNull(exception);
        assertInstanceOf(ResourceNotFoundException.class, exception);
        assertTrue(exception.getMessage().contains(UsuarioExceptionMessages.USUARIO_NAO_EXISTE));
        verify(usuarioRepository, never()).save(usuario);
    }

}
