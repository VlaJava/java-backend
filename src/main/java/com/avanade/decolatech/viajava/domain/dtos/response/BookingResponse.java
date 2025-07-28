package com.avanade.decolatech.viajava.domain.dtos.response;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID userId,
        UUID packageId,
        BigDecimal totalPrice,
        LocalDateTime bookingDate,
        LocalDate travelDate,
        String bookingStatus,
        List<TravelerResponse> travelers
) {}