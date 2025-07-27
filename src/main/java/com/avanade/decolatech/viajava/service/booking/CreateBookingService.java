package com.avanade.decolatech.viajava.service.booking;

import com.avanade.decolatech.viajava.domain.dtos.request.BookingRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.BookingResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.mapper.BookingMapper;
import com.avanade.decolatech.viajava.domain.model.Booking;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.model.enums.BookingStatus;
import com.avanade.decolatech.viajava.domain.repository.BookingRepository;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.service.user.GetUserByIdService;
import com.avanade.decolatech.viajava.utils.UserExceptionMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class CreateBookingService {

    private final BookingRepository bookingRepository;
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    public CreateBookingService(
            BookingRepository bookingRepository,
            PackageRepository packageRepository,
            UserRepository userRepository,
            BookingMapper bookingMapper
    ) {
        this.bookingRepository = bookingRepository;
        this.packageRepository = packageRepository;
        this.userRepository = userRepository;
        this.bookingMapper = bookingMapper;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request, UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("[%s execute] - %s",
                                        GetUserByIdService.class.getName(),
                                        UserExceptionMessages.USER_NOT_FOUND)
                        )
                );

        Package travelPackage = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        if (!travelPackage.isAvailable()) {
            throw new BusinessException("Package is not available");
        }

        LocalDate travelDate = request.getTravelDate();
        if (travelDate.isBefore(travelPackage.getStartDate()) || travelDate.isAfter(travelPackage.getEndDate())) {
            throw new BusinessException("The travel date is outside the available period for this package.");
        }

        Booking booking = bookingMapper.toBooking(request, user, travelPackage);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setBookingDate(java.time.LocalDateTime.now());
        System.out.println(booking.toString());
        Booking save = bookingRepository.save(booking);

        return bookingMapper.toBookingResponse(save);
    }
}
