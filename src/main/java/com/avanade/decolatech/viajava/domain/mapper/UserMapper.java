package com.avanade.decolatech.viajava.domain.mapper;

import com.avanade.decolatech.viajava.domain.dtos.request.user.CreateUserRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.user.CreateUserResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.user.UserResponse;
import com.avanade.decolatech.viajava.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;


import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {
    default CreateUserResponse toCreateUsuarioResponse(User user, String numeroDocumento) {
       return new CreateUserResponse(
                user.getId(), user.getName(), user.getEmail(), user.getPhone(), numeroDocumento
        );
    }

    @Mapping(source = "birthdate", target = "birthdate")
    User toUser(CreateUserRequest createUserRequest);

    UserResponse toUserResponse(User user);

    default PaginatedResponse<UserResponse> toPaginatedUsersResponse(Page<User> users) {
        List<UserResponse> usersResponse =
                users
                        .getContent()
                        .stream()
                        .filter(User::isActive)
                        .map(this::toUserResponse)
                        .toList();

        return new PaginatedResponse<>
                (usersResponse, users.getNumber(), users.getTotalElements(), users.getTotalPages());

    }

}
