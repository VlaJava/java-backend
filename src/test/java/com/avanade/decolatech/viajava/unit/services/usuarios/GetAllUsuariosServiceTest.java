package com.avanade.decolatech.viajava.unit.services.usuarios;

import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.service.user.GetUsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class GetAllUsuariosServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUsersService getAllUsuariosService;

    private List<User> users;

    @BeforeEach
    void setup() {
        users = new ArrayList<>();

        users.add(User.builder()
                .id(UUID.randomUUID())
                .nome("Maria")
                .email("maria@email.com")
                .senha("mary00534")
                .telefone("21992477508")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build());

        users.add(User.builder()
                .id(UUID.randomUUID())
                .nome("João")
                .email("joao@email.com")
                .senha("joao12345")
                .telefone("21992477509")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build());

        users.add(User.builder()
                .id(UUID.randomUUID())
                .nome("Ana")
                .email("ana@email.com")
                .senha("ana54321")
                .telefone("21992477510")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build());

        users.add(User.builder()
                .id(UUID.randomUUID())
                .nome("Carlos")
                .email("carlos@email.com")
                .senha("carlos2024")
                .telefone("21992477511")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build());

        users.add(User.builder()
                .id(UUID.randomUUID())
                .nome("Fernanda")
                .email("fernanda@email.com")
                .senha("fernanda321")
                .telefone("21992477512")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build());

        users.add(User.builder()
                .id(UUID.randomUUID())
                .nome("Lucas")
                .email("lucas@email.com")
                .senha("lucaspass")
                .telefone("21992477513")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build());

        users.add(User.builder()
                .id(UUID.randomUUID())
                .nome("Patricia")
                .email("patricia@email.com")
                .senha("patricia007")
                .telefone("21992477514")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build());

        users.add(User.builder()
                .id(UUID.randomUUID())
                .nome("Ricardo")
                .email("ricardo@email.com")
                .senha("ricardo123")
                .telefone("21992477515")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build());

        users.add(User.builder()
                .id(UUID.randomUUID())
                .nome("Sofia")
                .email("sofia@email.com")
                .senha("sofia321")
                .telefone("21992477516")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build());

        users.add(User.builder()
                .id(UUID.randomUUID())
                .nome("Thiago")
                .email("thiago@email.com")
                .senha("thiago654")
                .telefone("21992477517")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build());
    }

    @Test
    void getAllUsuarios_DeveRetornarTodosUsuarios_QuandoUsuariosExistirem() {
        int pageNumber = 0;
        int pageSize = 10;
        int expectedSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> usuarioPage = new PageImpl<>(users, pageable, users.size());

        given(userRepository.findAll(pageable)).willReturn(usuarioPage);

        Page<User> result = getAllUsuariosService.execute(pageNumber, pageSize);

        assertNotNull(result);
        assertInstanceOf(Page.class, result);
        assertEquals(expectedSize, result.getTotalElements());
        assertTrue(result.getContent().contains(users.getFirst()));
        assertTrue(result.getContent().contains(users.getLast()));
    }

}