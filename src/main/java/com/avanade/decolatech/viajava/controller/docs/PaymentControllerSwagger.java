package com.avanade.decolatech.viajava.controller.docs;

import com.avanade.decolatech.viajava.domain.dtos.request.payment.CreatePaymentRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.payment.MercadoPagoNotification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

public interface PaymentControllerSwagger {

    @Operation(summary = "Create a payment preference",
            description = "Creates a payment preference for a booking. Requires CLIENT role.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Preference successfully created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    ResponseEntity<Void> createPreference(
            @RequestBody @Valid CreatePaymentRequest createPaymentRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal com.avanade.decolatech.viajava.domain.model.User user
    );

    @Operation(summary = "Payment success endpoint",
            description = "Returns 'success' string. Accessible to all.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success response", content = @Content(mediaType = "text/plain"))
    })
    String success();

    @Operation(summary = "Payment fail endpoint",
            description = "Returns 'fail' string. Accessible to all.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fail response", content = @Content(mediaType = "text/plain"))
    })
    String fail();

    @Operation(summary = "Payment pending endpoint",
            description = "Returns 'pending' string. Accessible to all.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending response", content = @Content(mediaType = "text/plain"))
    })
    String pending();

    @Operation(summary = "Handle MercadoPago payment webhook notification",
            description = "Handles payment notifications from MercadoPago. Accessible without authentication.",
            security = @SecurityRequirement(name = "none"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification processed or ignored", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid notification", content = @Content)
    })
    ResponseEntity<Void> handleGatewayNotification(
            @RequestBody(required = false) MercadoPagoNotification notification
    );
}
