package com.avanade.decolatech.viajava.domain.dtos.response.pacote;

import com.avanade.decolatech.viajava.utils.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PackageResponse(
        UUID id,
        String title,
        String source,
        String destination,
        String description,
        String imageUrl,
        BigDecimal price,
        Integer travelerLimit,
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate startDate,
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate endDate,
        boolean available
) {}

