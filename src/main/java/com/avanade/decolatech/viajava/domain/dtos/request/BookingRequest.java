package com.avanade.decolatech.viajava.domain.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotNull(message = "Package ID é obrigatório")
    UUID packageId;

    //TODO: Ajustar para receber do contexto do usuário autenticado
    @NotNull(message = "A data de viagem é obrigatória")
    LocalDate travelDate;
}
