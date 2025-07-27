package com.avanade.decolatech.viajava.domain.mapper;

import com.avanade.decolatech.viajava.domain.dtos.request.BookingRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.BookingResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.model.Booking;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.model.enums.BookingStatus;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    default Booking toBooking(BookingRequest request, User user, Package travelPackage) {
        return Booking.builder()
                .user(user)
                .travelPackage(travelPackage)
                .travelDate(request.getTravelDate())
                .bookingStatus(BookingStatus.PENDING)
                .bookingDate(LocalDateTime.now())
                .totalPrice(travelPackage.getPrice())
                .build();
    }

    default BookingResponse toBookingResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUser() != null ? booking.getUser().getId() : null,
                booking.getTravelPackage() != null ? booking.getTravelPackage().getId() : null,
                booking.getTotalPrice(),
                booking.getBookingDate(),
                booking.getTravelDate(),
                booking.getBookingStatus().toString()
        );

    }

    default PaginatedResponse<BookingResponse> toPaginatedBookingResponse(Page<Booking> bookings) {

        List<BookingResponse> bookingResponses = bookings
                .getContent()
                .stream()
                .map(this::toBookingResponse)
                .toList();

        return new PaginatedResponse<>(
               bookingResponses,
                bookings.getNumber(),
                bookings.getTotalElements(),
                bookings.getTotalPages()
        );
    }
}
