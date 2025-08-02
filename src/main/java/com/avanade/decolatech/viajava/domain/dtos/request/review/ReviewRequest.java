package com.avanade.decolatech.viajava.domain.dtos.request.review;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record ReviewRequest(

        @NotNull
        UUID bookingId,

        @NotNull
        @Min(value = 1, message = "A avaliação deve ser no mínimo 1.")
        @Max(value = 5, message = "A avaliação deve ser no máximo 5.")
        Integer rating,

        @NotBlank
        @Size(max = 1000, message = "O comentário não pode exceder 1000 caracteres.")
        String comment
) {}
