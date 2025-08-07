package com.avanade.decolatech.viajava.domain.dtos.response.view;

import java.math.BigDecimal;
import java.util.UUID;

public interface PendingBookingClientsView {
    String getClientName();
    String getEmail();
    String getPhone();
    UUID getBookingId();
    BigDecimal getTotalPrice();
    BigDecimal getOutstandingBalance();
}
