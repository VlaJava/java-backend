package com.avanade.decolatech.viajava.domain.dtos.response.view;

import java.math.BigDecimal;

public interface MonthlyRevenueView {
    Integer getYear();
    Integer getMonth();
    Long getBookingCount();
    BigDecimal getRevenue();
}
