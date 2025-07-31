package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.controller.docs.BookingControllerSwagger;
import com.avanade.decolatech.viajava.domain.dtos.request.booking.BookingRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.booking.BookingResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.mapper.BookingMapper;
import com.avanade.decolatech.viajava.domain.model.Booking;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.service.booking.CreateBookingService;
import com.avanade.decolatech.viajava.service.booking.GetAllBookingService;
import com.avanade.decolatech.viajava.service.booking.GetAllBookingUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Bookings", description = "Operations related to bookings")
@RestController
@RequestMapping("/bookings")
public class BookingController implements BookingControllerSwagger {

    private final CreateBookingService createBookingService;
    private final GetAllBookingService getAllBookingService;
    private final BookingMapper bookingMapper;
    private final GetAllBookingUserService getAllBookingUserService;

    public BookingController(CreateBookingService createBookingService, GetAllBookingService getAllBookingService, BookingMapper bookingMapper, GetAllBookingUserService getAllBookingUserService) {
        this.createBookingService = createBookingService;
        this.getAllBookingService = getAllBookingService;
        this.bookingMapper = bookingMapper;
        this.getAllBookingUserService = getAllBookingUserService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        UUID userId = authenticatedUser.getId();
        BookingResponse response = this.createBookingService.createBooking(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<PaginatedResponse<BookingResponse>> getAllBookings(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size
    ){
        Page<Booking> bookings = this.getAllBookingService.getAllBooking(page, size);

        return ResponseEntity.ok(bookingMapper.toPaginatedBookingResponse(bookings));
    }

    @GetMapping("/user")
    public ResponseEntity<PaginatedResponse<BookingResponse>> getAllBookingsUser(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size,
            @AuthenticationPrincipal User authenticatedUser
    ){
        UUID userId = authenticatedUser.getId();

        Page<Booking> bookings = this.getAllBookingUserService.getAllBookingUser(page, size, userId);

        return ResponseEntity.ok(bookingMapper.toPaginatedBookingResponse(bookings));
    }

}
