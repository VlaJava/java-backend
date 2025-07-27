package com.avanade.decolatech.viajava.service.booking;

import com.avanade.decolatech.viajava.domain.model.Booking;
import com.avanade.decolatech.viajava.domain.repository.BookingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetAllBookingService {
    private final BookingRepository bookingRepository;

    public GetAllBookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public Page<Booking> getAllBooking(Integer page, Integer size) {
        return bookingRepository.findAll(PageRequest.of(page, size));
    }

}

