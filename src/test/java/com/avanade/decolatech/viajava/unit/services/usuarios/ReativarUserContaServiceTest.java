package com.avanade.decolatech.viajava.unit.services.usuarios;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.service.user.ReactivateUserAccountService;
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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReativarUserContaServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReactivateUserAccountService reactivateUserAccountService;

    private User user;

    @BeforeEach
    void setUp() {
      this.user =  User.builder()
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
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        reactivateUserAccountService.execute(user.getId());

        assertEquals(expectedStatus, user.isAtivo());
    }

    @Test
    void reativarUsuario_DeveNaoFazerNada_QuandoUsuarioJaEstiverAtivo() {
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        user.setAtivo(true);

        reactivateUserAccountService.execute(user.getId());

        verify(userRepository, never()).save(user);
    }

    @Test
    void reativarUsuario_DeveDispararResourceNotFoundException_QuandoUsuarioNaoExistir() {
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            reactivateUserAccountService.execute(user.getId());
        });

        assertNotNull(exception);
        assertInstanceOf(ResourceNotFoundException.class, exception);
        assertTrue(exception.getMessage().contains(UserExceptionMessages.USER_NOT_FOUND));
        verify(userRepository, never()).save(user);
    }
}