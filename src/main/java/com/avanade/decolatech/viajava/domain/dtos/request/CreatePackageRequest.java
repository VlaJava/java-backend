package com.avanade.decolatech.viajava.domain.dtos.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreatePackageRequest {

    @NotBlank
    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres.")
    private String title;

    @NotBlank
    @Size(max = 100, message = "A origem deve ter no máximo 100 caracteres.")
    private String source;

    @NotBlank
    @Size(max = 100, message = "O destino deve ter no máximo 100 caracteres.")
    private String destination;

    @NotBlank
    private String description;

    private String imageUrl;

    @NotNull
    @Positive(message = "O valor deve ser maior que zero.")
    private BigDecimal price;

    @NotNull
    @Min(value = 1, message = "O pacote deve permitir pelo menos 1 viajante.")
    private Integer travelerLimit;

    @NotNull
    @FutureOrPresent(message = "A data de início não pode ser no passado.")
    private LocalDate startDate;

    @NotNull
    @Future(message = "A data de fim deve ser uma data futura.")
    private LocalDate endDate;
}
