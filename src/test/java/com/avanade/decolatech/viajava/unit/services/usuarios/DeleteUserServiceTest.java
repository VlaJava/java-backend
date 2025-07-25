package com.avanade.decolatech.viajava.unit.services.usuarios;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.service.user.DeleteUserService;
import com.avanade.decolatech.viajava.utils.UserExceptionMessages;
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
public class DeleteUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteUserService deleteUserService;

    private User user;

    @BeforeEach
    void setup() {
        user = User
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
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        deleteUserService.execute(user.getId());

        assertEquals(expectedStatus, user.isAtivo());

    }

    @Test
    void deleteUsuario_DeveNaoFazerNada_QuandoUsuarioJaEstiverInativo() {
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        user.setAtivo(false);

        deleteUserService.execute(user.getId());

        verify(userRepository, never()).save(user);
    }

    @Test
    void deleteUsuario_DeveDispararResourceNotFoundException_QuandoUsuarioNaoExistir() {
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deleteUserService.execute(user.getId());
        });

        assertNotNull(exception);
        assertInstanceOf(ResourceNotFoundException.class, exception);
        assertTrue(exception.getMessage().contains(UserExceptionMessages.USER_NOT_FOUND));
        verify(userRepository, never()).save(user);
    }

}
