package com.avanade.decolatech.viajava.service.booking;

import com.avanade.decolatech.viajava.domain.dtos.request.booking.BookingRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.booking.BookingResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.mapper.BookingMapper;
import com.avanade.decolatech.viajava.domain.mapper.TravelerMapper;
import com.avanade.decolatech.viajava.domain.model.Booking;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.model.Traveler;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.BookingRepository;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class CreateBookingService {

    private final BookingRepository bookingRepository;
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final TravelerMapper travelerMapper;

    public CreateBookingService(
            BookingRepository bookingRepository,
            PackageRepository packageRepository,
            UserRepository userRepository,
            BookingMapper bookingMapper,
            TravelerMapper travelerMapper
    ) {
        this.bookingRepository = bookingRepository;
        this.packageRepository = packageRepository;
        this.userRepository = userRepository;
        this.bookingMapper = bookingMapper;
        this.travelerMapper = travelerMapper;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request, UUID userId) {
        User user = findUserById(userId);
        Package travelPackage = findPackageById(request.getPackageId());

        validateBookingRules(request, travelPackage);

        Booking booking = buildBooking(request, user, travelPackage);

        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toBookingResponse(savedBooking);
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    private Package findPackageById(UUID packageId) {
        return packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with ID: " + packageId));
    }

    private void validateBookingRules(BookingRequest request, Package travelPackage) {
        if (!travelPackage.isAvailable()) {
            throw new BusinessException("Package is not available");
        }

        LocalDate travelDate = request.getTravelDate();

        LocalDate bookingDate = LocalDate.now();
        if (!travelDate.isAfter(bookingDate)) {
            throw new BusinessException("Travel date must be after the booking date.");
        }

        if (travelDate.isBefore(travelPackage.getStartDate()) || travelDate.isAfter(travelPackage.getEndDate())) {
            throw new BusinessException("The travel date is outside the available period for this package.");
        }

        if (request.getTravelers().size() > travelPackage.getTravelerLimit()) {
            throw new BusinessException(
                    String.format(
                            "Number of travelers (%d) exceeds the package limit (%d).",
                            request.getTravelers().size(),
                            travelPackage.getTravelerLimit()
                    )
            );
        }
    }

    private Booking buildBooking(BookingRequest request, User user, Package travelPackage) {
        Booking booking = bookingMapper.toBooking(request, user, travelPackage);

        AtomicInteger numberOfTravelersOverEighteen = new AtomicInteger();
        int currentYear = LocalDate.now().getYear();

        List<Traveler> travelers = request.getTravelers().stream()
                .map(travelerRequest -> {
                    if (currentYear - travelerRequest.birthdate().getYear() >= 18) {
                        numberOfTravelersOverEighteen.getAndIncrement();
                    }
                    Traveler traveler = travelerMapper.toTraveler(travelerRequest);
                    traveler.setBooking(booking);
                    return traveler;
                })
                .collect(Collectors.toList());

        if (numberOfTravelersOverEighteen.get() == 0) {
            throw new BusinessException(
                    "Is not possible to create an booking without at least one of the travelers over 18 years.");
        }

        booking.setTravelers(travelers);

        BigDecimal numberOfTravelers = new BigDecimal(travelers.size());
        booking.setTotalPrice(travelPackage.getPrice().multiply(numberOfTravelers));

        return booking;
    }
}
