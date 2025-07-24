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
public class UpdatePacoteRequest {

    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres.")
    private String titulo;

    @Size(max = 100, message = "A origem deve ter no máximo 100 caracteres.")
    private String origem;

    @Size(max = 100, message = "O destino deve ter no máximo 100 caracteres.")
    private String destino;

    private String descricao;

    private String imagemUrl;

    @Positive(message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    @Min(value = 1, message = "O pacote deve permitir pelo menos 1 viajante.")
    private Integer limiteViajantes;

    @FutureOrPresent(message = "A data de início não pode ser no passado.")
    private LocalDate dataInicio;

    @Future(message = "A data de fim deve ser uma data futura.")
    private LocalDate dataFim;
}
