package com.avanade.decolatech.viajava.domain.dtos.response.review;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        Integer rating,
        String comment,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate reviewDate,
        String userName,
        String imageUrl
) {}
