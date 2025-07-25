package com.avanade.decolatech.viajava.domain.mapper;

import com.avanade.decolatech.viajava.domain.dtos.request.CreateUserRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUserResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedUserResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.UserResponse;
import com.avanade.decolatech.viajava.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;


import java.util.List;


@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    default CreateUserResponse toCreateUsuarioResponse(User user, String numeroDocumento) {
       return new CreateUserResponse(
                user.getId(), user.getNome(), user.getEmail(), user.getTelefone(), numeroDocumento
        );
    }

    @Mapping(source = "dataNasc", target = "dataNasc")
    User toUsuario(CreateUserRequest createUserRequest);

    UserResponse toUsuarioResponse(User user);

    default PaginatedUserResponse toPaginatedUsuarioResponse(Page<User> usuarios) {
        List<UserResponse> usuariosResponse =
                usuarios
                        .getContent()
                        .stream()
                        .filter(User::isAtivo)
                        .map(this::toUsuarioResponse)
                        .toList();

        return
                PaginatedUserResponse
                        .builder()
                        .usuarios(usuariosResponse)
                        .current_page(usuarios.getNumber())
                        .total_items(usuarios.getTotalElements())
                        .total_pages(usuarios.getTotalPages())
                        .build();
    }

}
