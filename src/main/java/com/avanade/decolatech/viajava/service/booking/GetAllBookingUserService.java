package com.avanade.decolatech.viajava.service.booking;

import com.avanade.decolatech.viajava.domain.model.Booking;
import com.avanade.decolatech.viajava.domain.repository.BookingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetAllBookingUserService {

    private final BookingRepository bookingRepository;

    public GetAllBookingUserService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Page<Booking> getAllBookingUser(Integer page, Integer size, UUID userId) {
        return bookingRepository.findAllByUserId(userId, PageRequest.of(page, size));
    }
}

