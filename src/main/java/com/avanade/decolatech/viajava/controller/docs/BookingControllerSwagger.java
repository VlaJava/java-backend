package com.avanade.decolatech.viajava.controller.docs;

import com.avanade.decolatech.viajava.domain.dtos.request.booking.BookingRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.booking.BookingResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "Bookings", description = "Operations related to bookings")
public interface BookingControllerSwagger {

    @Operation(summary = "Create a new booking",
            description = "Creates a new booking for the authenticated user.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Booking successfully created",
                content = @Content(schema = @Schema(implementation = BookingResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal com.avanade.decolatech.viajava.domain.model.User authenticatedUser
    );

    @Operation(summary = "Get all bookings (admin)",
            description = "Returns a paginated list of all bookings for Admin Panel.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of bookings",
                content = @Content(schema = @Schema(implementation = PaginatedResponse.class)))
    })
    ResponseEntity<PaginatedResponse<BookingResponse>> getAllBookings(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size
    );

    @Operation(summary = "Get all bookings for user",
            description = "Returns a paginated list of bookings for My Bookings.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of user bookings",
                content = @Content(schema = @Schema(implementation = PaginatedResponse.class)))
    })
    ResponseEntity<PaginatedResponse<BookingResponse>> getAllBookingsUser(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size,
            @Parameter(hidden = true) @AuthenticationPrincipal com.avanade.decolatech.viajava.domain.model.User authenticatedUser
    );
}
