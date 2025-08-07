package com.avanade.decolatech.viajava.utils;

public interface PaymentExceptionMessages {
    String PAYER_ID_DIFFERENT = "The provided payer id is different of the user id that created the booking.";
    String USER_IS_INACTIVE = "A user that is inactive cannot pay a booking.";
    String BOOKING_ALREADY_CONFIRMED = "You cannot pay for a booking that already have been confirmed.";
    String BOOKING_CANCELLED = "You cannot pay for a booking that already have been cancelled.";
}
