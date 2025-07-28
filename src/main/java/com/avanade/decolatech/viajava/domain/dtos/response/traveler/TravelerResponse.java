package com.avanade.decolatech.viajava.domain.dtos.response.traveler;

import java.time.LocalDate;
import java.util.UUID;

public record TravelerResponse(
        UUID id,
        String name,
        String document,
        LocalDate birthdate
) {}

