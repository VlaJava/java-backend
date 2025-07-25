package com.avanade.decolatech.viajava.domain.dtos.response;

import com.avanade.decolatech.viajava.domain.model.Usuario;
import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedUsuarioResponse {
    List<UsuarioResponse> usuarios;
    int current_page;
    long total_items;
    int total_pages;
}
