package com.avanade.decolatech.viajava.domain.dtos.response.booking;


import com.avanade.decolatech.viajava.domain.dtos.response.traveler.TravelerResponse;
import com.avanade.decolatech.viajava.utils.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
        String orderNumber,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime bookingDate,

        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate travelDate,

        String bookingStatus,
        List<TravelerResponse> travelers
) {}
