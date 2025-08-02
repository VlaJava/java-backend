package com.avanade.decolatech.viajava.service.review;

import com.avanade.decolatech.viajava.domain.dtos.request.review.RatingCount;
import com.avanade.decolatech.viajava.domain.dtos.response.review.ReviewStatsResponse;
import com.avanade.decolatech.viajava.domain.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GetReviewStatsService {
    private final ReviewRepository reviewRepository;

    public GetReviewStatsService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public ReviewStatsResponse execute(UUID packageId) {
        List<RatingCount> summary = reviewRepository.findRatingSummaryByPackageId(packageId);

        if (summary.isEmpty()) {
            return new ReviewStatsResponse(0.0, 0, createEmptyRatingMap());
        }

        long totalReviews = summary.stream().mapToLong(RatingCount::count).sum();
        long weightedSum = summary.stream().mapToLong(s -> s.rating() * s.count()).sum();
        double averageRating = (double) weightedSum / totalReviews;

        Map<Integer, Long> ratingCounts = createEmptyRatingMap();
        summary.forEach(s -> ratingCounts.put(s.rating(), s.count()));

        double formattedAverage = Math.round(averageRating * 100.0) / 100.0;

        return new ReviewStatsResponse(formattedAverage, totalReviews, ratingCounts);
    }

    private Map<Integer, Long> createEmptyRatingMap() {
        return IntStream.rangeClosed(1, 5)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), i -> 0L));
    }
}