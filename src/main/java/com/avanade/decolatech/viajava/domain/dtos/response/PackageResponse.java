package com.avanade.decolatech.viajava.domain.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PackageResponse(
        UUID id,
        String titulo,
        String origem,
        String destino,
        String descricao,
        String imagemUrl,
        BigDecimal valor,
        Integer limiteViajantes,
        LocalDate dataInicio,
        LocalDate dataFim,
        boolean disponivel
) {
}
