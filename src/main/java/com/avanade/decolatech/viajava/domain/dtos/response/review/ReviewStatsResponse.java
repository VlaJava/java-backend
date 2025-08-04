package com.avanade.decolatech.viajava.domain.dtos.response.review;

import java.util.Map;

public record ReviewStatsResponse(
        double averageRating,
        long totalReviews,
        Map<Integer, Long> ratingCounts
) {}

