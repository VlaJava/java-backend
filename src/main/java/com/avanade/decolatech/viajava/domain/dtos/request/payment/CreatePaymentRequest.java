package com.avanade.decolatech.viajava.domain.dtos.request.payment;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePaymentRequest
        (
                @NotNull
                @JsonProperty("booking_id")
                UUID bookingId
        ){
}
