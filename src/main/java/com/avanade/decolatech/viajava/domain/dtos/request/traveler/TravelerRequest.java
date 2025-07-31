package com.avanade.decolatech.viajava.domain.dtos.request.traveler;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TravelerRequest(
        @NotBlank
        String name,

        @NotBlank
        @Size(min = 8, max = 11, message = "The document number must have 11 characters if be cpf or 8 characters if be passport.")
        String document,

        @NotNull
        @Past(message = "Birthdate must be in the past")
        LocalDate birthdate
) {}
