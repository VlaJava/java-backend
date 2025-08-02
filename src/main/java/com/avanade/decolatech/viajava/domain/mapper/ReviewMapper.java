package com.avanade.decolatech.viajava.domain.mapper;

import com.avanade.decolatech.viajava.domain.dtos.response.review.ReviewResponse;
import com.avanade.decolatech.viajava.domain.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "booking.user.name", target = "userName")
    @Mapping(source = "booking.user.imageUrl", target = "imageUrl")
    ReviewResponse toResponse(Review review);

    List<ReviewResponse> toResponseList(List<Review> reviews);
}

