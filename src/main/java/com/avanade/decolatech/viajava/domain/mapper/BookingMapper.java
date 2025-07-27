package com.avanade.decolatech.viajava.domain.mapper;

import com.avanade.decolatech.viajava.domain.dtos.request.BookingRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.BookingResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.model.Booking;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    default Booking toBooking(BookingRequest request, User user, Package travelPackage) {
        return Booking.builder()
                .user(user)
                .travelPackage(travelPackage)
                .travelDate(request.getTravelDate())
                .totalPrice(travelPackage.getPrice())
                .build();
    }

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "travelPackage.id", target = "packageId")
    BookingResponse toBookingResponse(Booking booking);

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
