package com.avanade.decolatech.viajava.domain.dtos.request.payment;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CreatePreferenceRequest(
        UUID userId,

        BigDecimal totalAmount,

        PayerDTO payer,

        BackUrlsDTO backUrls,

        PackageDTO travelPackage) {

    public record PayerDTO(

            String name,

            String email
    ) {}

    public record BackUrlsDTO(
            String success,

            String failure,

            String pending
    ) {}

    public record PackageDTO(
            UUID id,
            String title,
            String description,
            Integer quantityOfTravelers,
            BigDecimal unitPrice
    ) {}

}

