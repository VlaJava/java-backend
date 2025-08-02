package com.avanade.decolatech.viajava.domain.dtos.response.view;

import java.math.BigDecimal;

public interface GeneralMetricsView {
    Long getActiveClients();
    Long getConfirmedBookings();
    BigDecimal getTotalRevenue();
    Double getAvgRatings();
    Long getAvailablePackages();
}
