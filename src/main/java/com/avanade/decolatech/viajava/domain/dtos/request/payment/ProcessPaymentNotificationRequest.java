package com.avanade.decolatech.viajava.domain.dtos.request.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessPaymentNotificationRequest {
    /**
     * e.g, "payment"
     */
    String resourceType;

    /**
     * e.g, the payment ID.
     */
    String resourceId;
}
