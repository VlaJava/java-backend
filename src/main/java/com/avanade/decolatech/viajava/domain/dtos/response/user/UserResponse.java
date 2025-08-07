package com.avanade.decolatech.viajava.domain.dtos.response.user;

import com.avanade.decolatech.viajava.domain.model.Role;


import java.util.UUID;

public record UserResponse(
        UUID id, String name, String email, String phone, String birthdate, Role role, boolean active
) {
}
