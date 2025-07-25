package com.avanade.decolatech.viajava.domain.dtos.response;

import java.util.UUID;

public record UsuarioResponse(
        UUID id, String nome, String email, String telefone, String imagemPerfil, boolean ativo
) {
}
