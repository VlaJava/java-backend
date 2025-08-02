package com.avanade.decolatech.viajava.service.review;

import com.avanade.decolatech.viajava.domain.dtos.response.review.ReviewResponse;
import com.avanade.decolatech.viajava.domain.mapper.ReviewMapper;
import com.avanade.decolatech.viajava.domain.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetReviewByPackageService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public GetReviewByPackageService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    public List<ReviewResponse> execute(UUID packageId) {
        var reviews = reviewRepository.findByPackageId(packageId);
        return reviewMapper.toResponseList(reviews);
    }

}
