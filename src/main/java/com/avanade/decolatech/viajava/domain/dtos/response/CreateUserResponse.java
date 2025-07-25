package com.avanade.decolatech.viajava.domain.dtos.response;

import java.util.UUID;

public record CreateUsuarioResponse(
        UUID id, String nome, String email, String telefone, String numeroDocumento
) {
}
