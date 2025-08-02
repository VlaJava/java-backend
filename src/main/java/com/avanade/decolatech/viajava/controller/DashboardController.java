package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.controller.docs.DashboardControllerSwagger;
import com.avanade.decolatech.viajava.domain.dtos.response.view.GeneralMetricsView;
import com.avanade.decolatech.viajava.domain.dtos.response.view.MonthlyRevenueView;
import com.avanade.decolatech.viajava.domain.dtos.response.view.PendingBookingClientsView;
import com.avanade.decolatech.viajava.domain.dtos.response.view.PopularDestinationView;
import com.avanade.decolatech.viajava.service.dashboard.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController implements DashboardControllerSwagger {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Override
    @GetMapping("/general-metrics")
    public ResponseEntity<GeneralMetricsView> getGeneralMetrics() {
        return ResponseEntity.ok(dashboardService.getGeneralMetrics());
    }

    @Override
    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<MonthlyRevenueView>> getMonthlyRevenue() {
        return ResponseEntity.ok(dashboardService.getMonthlyRevenue());
    }

    @Override
    @GetMapping("/popular-destinations")
    public ResponseEntity<List<PopularDestinationView>> getPopularDestinations() {
        return ResponseEntity.ok(dashboardService.getPopularDestinations());
    }

    @Override
    @GetMapping("/pending-booking-clients")
    public ResponseEntity<List<PendingBookingClientsView>> getPendingBookingClients() {
        return ResponseEntity.ok(dashboardService.getPendingBookingClients());
    }
}
