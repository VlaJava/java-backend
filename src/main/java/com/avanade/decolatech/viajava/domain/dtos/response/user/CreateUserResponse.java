package com.avanade.decolatech.viajava.domain.dtos.response.user;

import java.util.UUID;

public record CreateUserResponse(
        UUID id, String name, String email, String phone, String documentNumber
) {
}
