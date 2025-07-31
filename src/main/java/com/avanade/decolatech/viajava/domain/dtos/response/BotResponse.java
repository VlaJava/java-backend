package com.avanade.decolatech.viajava.domain.dtos.response;

import java.util.List;

public record BotResponse(String text, List<PackageResponse> recommendedPackages) {

}
