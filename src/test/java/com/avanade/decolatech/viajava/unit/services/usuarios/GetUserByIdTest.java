package com.avanade.decolatech.viajava.unit.services.usuarios;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.service.user.GetUserByIdService;
import com.avanade.decolatech.viajava.utils.UserExceptionMessages;
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
public class GetUserByIdTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserByIdService getUserByIdService;

    private User user;

    @BeforeEach
    void setup() {
        user = User
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
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        User resultado = getUserByIdService.execute(user.getId());

        assertNotNull(resultado);
        assertInstanceOf(User.class, resultado);
        assertEquals(user.getId(), resultado.getId());
        assertEquals(user.getNome(), resultado.getNome());
        assertEquals(user.getEmail(), resultado.getEmail());
        assertEquals(user.getTelefone(), resultado.getTelefone());
        assertEquals(user.isAtivo(), resultado.isAtivo());
        assertEquals(user.getDataCadastro(), resultado.getDataCadastro());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void getUsuarioById_DeveLancarResourceNotFoundException_QuandoUsuarioNaoExistir() {
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            getUserByIdService.execute(user.getId());
        });

        assertNotNull(exception);
        assertInstanceOf(ResourceNotFoundException.class, exception);
        assertTrue(exception.getMessage().contains(UserExceptionMessages.USER_NOT_FOUND));
        verify(userRepository, times(1)).findById(user.getId());
    }

}
