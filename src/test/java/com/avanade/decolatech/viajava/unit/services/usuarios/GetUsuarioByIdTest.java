package com.avanade.decolatech.viajava.unit.services.usuarios;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import com.avanade.decolatech.viajava.service.GetUsuarioByIdService;
import com.avanade.decolatech.viajava.utils.UsuarioExceptionMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class GetUsuarioByIdTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private GetUsuarioByIdService getUsuarioByIdService;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = Usuario
                .builder()
                .id(UUID.randomUUID())
                .nome("Isabella")
                .email("isabella@avanade.com")
                .senha("isabella123578et")
                .telefone("82995262639")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build();
    }

    @Test
    void getUsuarioById_DeveRetornarUsuario_QuandoUsuarioExistir() {
        given(usuarioRepository.findById(usuario.getId())).willReturn(Optional.of(usuario));

        Usuario resultado = getUsuarioByIdService.execute(usuario.getId());

        assertNotNull(resultado);
        assertInstanceOf(Usuario.class, resultado);
        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        assertEquals(usuario.getTelefone(), resultado.getTelefone());
        assertEquals(usuario.isAtivo(), resultado.isAtivo());
        assertEquals(usuario.getDataCadastro(), resultado.getDataCadastro());
        verify(usuarioRepository, times(1)).findById(usuario.getId());
    }

    @Test
    void getUsuarioById_DeveLancarResourceNotFoundException_QuandoUsuarioNaoExistir() {
        given(usuarioRepository.findById(usuario.getId())).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            getUsuarioByIdService.execute(usuario.getId());
        });

        assertNotNull(exception);
        assertInstanceOf(ResourceNotFoundException.class, exception);
        assertTrue(exception.getMessage().contains(UsuarioExceptionMessages.USUARIO_NAO_EXISTE));
        verify(usuarioRepository, times(1)).findById(usuario.getId());
    }

}
