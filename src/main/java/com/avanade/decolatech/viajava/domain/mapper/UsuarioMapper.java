package com.avanade.decolatech.viajava.domain.mapper;

import com.avanade.decolatech.viajava.domain.dtos.request.CreateUsuarioRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUsuarioResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedUsuarioResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.UsuarioResponse;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    default CreateUsuarioResponse toCreateUsuarioResponse(Usuario usuario, String numeroDocumento) {
       return new CreateUsuarioResponse(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTelefone(), numeroDocumento
        );
    }
    Usuario toUsuario(CreateUsuarioRequest createUsuarioRequest);

    UsuarioResponse toUsuarioResponse(Usuario usuario);

    default PaginatedUsuarioResponse toPaginatedUsuarioResponse(Page<Usuario> usuarios) {
        List<UsuarioResponse> usuariosResponse =
                usuarios
                        .getContent()
                        .stream()
                        .filter(Usuario::isAtivo)
                        .map(this::toUsuarioResponse)
                        .toList();

        return
                PaginatedUsuarioResponse
                        .builder()
                        .usuarios(usuariosResponse)
                        .current_page(usuarios.getNumber())
                        .total_items(usuarios.getTotalElements())
                        .total_pages(usuarios.getTotalPages())
                        .build();
    }

}
