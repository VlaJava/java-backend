package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.controller.docs.ReviewControllerSwagger;
import com.avanade.decolatech.viajava.domain.dtos.request.review.ReviewRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.review.ReviewResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.review.ReviewStatsResponse;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.service.review.CreateReviewService;
import com.avanade.decolatech.viajava.service.review.GetReviewByPackageService;
import com.avanade.decolatech.viajava.service.review.GetReviewStatsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Reviews", description = "Endpoints for creating and retrieving user reviews")
public class ReviewController implements ReviewControllerSwagger {
    private final CreateReviewService createReviewService;
    private final GetReviewByPackageService getReviewByPackageService;
    private final GetReviewStatsService getReviewStatsService;

    public ReviewController(CreateReviewService createReviewService,
                            GetReviewByPackageService getReviewByPackageService,
                            GetReviewStatsService getReviewStatsService) {
        this.createReviewService = createReviewService;
        this.getReviewByPackageService = getReviewByPackageService;
        this.getReviewStatsService = getReviewStatsService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request,
                                                       @AuthenticationPrincipal User authenticatedUser) {

        UUID userId = authenticatedUser.getId();
        ReviewResponse response = createReviewService.execute(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/package/{packageId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByPackage(@PathVariable UUID packageId) {
        List<ReviewResponse> reviews = getReviewByPackageService.execute(packageId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/package/{packageId}/stats")
    public ResponseEntity<ReviewStatsResponse> getReviewStatsByPackage(@PathVariable UUID packageId) {
        ReviewStatsResponse stats = getReviewStatsService.execute(packageId);
        return ResponseEntity.ok(stats);
    }
}
