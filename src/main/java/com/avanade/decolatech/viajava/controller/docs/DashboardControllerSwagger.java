package com.avanade.decolatech.viajava.controller.docs;

import com.avanade.decolatech.viajava.domain.dtos.response.view.GeneralMetricsView;
import com.avanade.decolatech.viajava.domain.dtos.response.view.MonthlyRevenueView;
import com.avanade.decolatech.viajava.domain.dtos.response.view.PendingBookingClientsView;
import com.avanade.decolatech.viajava.domain.dtos.response.view.PopularDestinationView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "Dashboard", description = "Admin dashboard metrics and analytics")
public interface DashboardControllerSwagger {

    @Operation(
            summary = "Get general metrics",
            description = "Returns general platform statistics.",
            security = @SecurityRequirement(name = "security")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "General metrics fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (missing or invalid token)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user lacks ADMIN role)")
    })
    @GetMapping("/general-metrics")
    ResponseEntity<GeneralMetricsView> getGeneralMetrics();

    @Operation(
            summary = "Get monthly revenue",
            description = "Returns monthly revenue statistics.",
            security = @SecurityRequirement(name = "security")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monthly revenue data fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (missing or invalid token)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user lacks ADMIN role)")
    })
    @GetMapping("/monthly-revenue")
    ResponseEntity<List<MonthlyRevenueView>> getMonthlyRevenue();

    @Operation(
            summary = "Get popular destinations",
            description = "Returns a list of most popular destinations based on bookings.",
            security = @SecurityRequirement(name = "security")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Popular destinations fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (missing or invalid token)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user lacks ADMIN role)")
    })
    @GetMapping("/popular-destinations")
    ResponseEntity<List<PopularDestinationView>> getPopularDestinations();

    @Operation(
            summary = "Get clients with pending bookings",
            description = "Returns clients with pending booking confirmations.",
            security = @SecurityRequirement(name = "security")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending booking clients fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (missing or invalid token)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user lacks ADMIN role)")
    })
    @GetMapping("/pending-booking-clients")
    ResponseEntity<List<PendingBookingClientsView>> getPendingBookingClients();
}
