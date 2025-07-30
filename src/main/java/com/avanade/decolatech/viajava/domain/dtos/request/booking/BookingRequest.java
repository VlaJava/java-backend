package com.avanade.decolatech.viajava.domain.dtos.request.booking;

import com.avanade.decolatech.viajava.domain.dtos.request.traveler.TravelerRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
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

    @NotNull(message = "A data de viagem é obrigatória")
    LocalDate travelDate;

    @NotEmpty(message = "At least one traveler is required")
    @Valid
    List<TravelerRequest> travelers;

}
