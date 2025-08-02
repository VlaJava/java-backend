package com.avanade.decolatech.viajava.controller.docs;

import com.avanade.decolatech.viajava.domain.dtos.request.review.ReviewRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.review.ReviewResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.review.ReviewStatsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface ReviewControllerSwagger {

    @Operation(summary = "Create a new review",
            description = "Allows an authenticated user to create a review for a confirmed booking they have made. The user must be the owner of the booking.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided", content = @Content),
            @ApiResponse(responseCode = "403", description = "User not authorized to review this booking", content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content)
    })
    ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request,
                                                @Parameter(hidden = true) @AuthenticationPrincipal com.avanade.decolatech.viajava.domain.model.User authenticatedUser);

    @Operation(summary = "Get reviews by package ID",
            description = "Retrieves a list of all public reviews for a specific travel package.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of reviews",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "404", description = "Package not found or has no reviews", content = @Content)
    })
    ResponseEntity<List<ReviewResponse>> getReviewsByPackage(@PathVariable UUID packageId);

    @Operation(summary = "Get review statistics by package ID",
            description = "Retrieves aggregated statistics for a package's reviews, including average rating, total count, and a breakdown of ratings from 1 to 5.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved review statistics",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewStatsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Package not found", content = @Content)
    })
    ResponseEntity<ReviewStatsResponse> getReviewStatsByPackage(@PathVariable UUID packageId);

}
