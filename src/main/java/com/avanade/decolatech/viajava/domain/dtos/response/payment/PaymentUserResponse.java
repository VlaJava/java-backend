package com.avanade.decolatech.viajava.domain.dtos.response.payment;

import com.avanade.decolatech.viajava.domain.model.enums.DomainPaymentMethod;
import com.avanade.decolatech.viajava.domain.model.enums.DomainPaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentUserResponse(
        @JsonProperty("payment_id")
        UUID paymentId,
        DomainPaymentMethod method,
        BigDecimal amount,
        @JsonProperty("payment_date")
        LocalDateTime paymentDate,
        @JsonProperty("payment_status")
        DomainPaymentStatus status
) {
}
