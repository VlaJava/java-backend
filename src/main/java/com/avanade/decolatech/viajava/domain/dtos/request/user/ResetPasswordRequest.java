package com.avanade.decolatech.viajava.domain.dtos.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank
    @Size(min = 8, message = "The password must have at least 8 characters.")
    private String password;

    @NotBlank
    @Size(min = 8, message = "The password confirmation must have at least 8 characters.")
    private String passwordConfirmation;
}
