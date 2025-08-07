package com.avanade.decolatech.viajava.domain.dtos.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateRoleRequest(
        @NotBlank
        @Pattern(regexp = "CLIENT|ADMIN")
        String userRole,

        @NotBlank
        @Email
        String email
) {
}
