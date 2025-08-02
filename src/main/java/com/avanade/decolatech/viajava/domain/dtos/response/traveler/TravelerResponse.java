package com.avanade.decolatech.viajava.domain.dtos.response.traveler;

import com.avanade.decolatech.viajava.utils.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;
import java.util.UUID;

public record TravelerResponse(
        UUID id,
        String name,
        String document,

        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate birthdate
) {}

