package com.avanade.decolatech.viajava.domain.dtos.response;

import com.avanade.decolatech.viajava.domain.dtos.response.pacote.PackageResponse;

import java.util.List;

public record BotResponse(String text, List<PackageResponse> recommendedPackages) {

}
