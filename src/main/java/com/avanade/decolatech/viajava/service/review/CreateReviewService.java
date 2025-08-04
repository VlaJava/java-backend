package com.avanade.decolatech.viajava.service.review;

import com.avanade.decolatech.viajava.domain.dtos.request.review.ReviewRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.review.ReviewResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.mapper.ReviewMapper;
import com.avanade.decolatech.viajava.domain.model.Booking;
import com.avanade.decolatech.viajava.domain.model.Review;
import com.avanade.decolatech.viajava.domain.model.enums.BookingStatus;
import com.avanade.decolatech.viajava.domain.repository.BookingRepository;
import com.avanade.decolatech.viajava.domain.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ReviewMapper reviewMapper;

    public CreateReviewService(ReviewRepository reviewRepository, BookingRepository bookingRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    public ReviewResponse execute(ReviewRequest request, UUID authenticatedUserId) {
        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada."));

        if (!booking.getUser().getId().equals(authenticatedUserId)) {
            throw new BusinessException("Você não tem permissão para avaliar esta reserva.");
        }

        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new BusinessException("Só é possível avaliar reservas com status CONFIRMADO.");
        }

        Review review = Review.builder()
                .booking(booking)
                .rating(request.rating())
                .comment(request.comment())
                .build();

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toResponse(savedReview);
    }
}
