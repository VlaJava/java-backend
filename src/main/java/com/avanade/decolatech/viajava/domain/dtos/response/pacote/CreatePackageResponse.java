package com.avanade.decolatech.viajava.domain.dtos.response.pacote;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreatePackageResponse(
        UUID id,
        String title,
        String source,
        String destination,
        String description,
        String imageUrl,
        BigDecimal price,
        int travelerLimit,
        LocalDate startDate,
        LocalDate endDate,
        boolean available
) {}