package com.avanade.decolatech.viajava.domain.dtos.response.view;

import java.math.BigDecimal;

public interface PopularDestinationView {
    String getDestination();
    Long getBookings();
    BigDecimal getRevenue();
}
