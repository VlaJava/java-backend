package com.avanade.decolatech.viajava.domain.dtos.response;

import java.util.UUID;

public record UserResponse(
        UUID id, String name, String email, String phone, boolean active
) {
}
