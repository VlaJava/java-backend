package com.avanade.decolatech.viajava.domain.repository;

import com.avanade.decolatech.viajava.domain.dtos.request.review.RatingCount;
import com.avanade.decolatech.viajava.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Query("SELECT r FROM Review r WHERE r.booking.travelPackage.id = :packageId")
    List<Review> findByPackageId(@Param("packageId") UUID packageId);

    @Query("SELECT new com.avanade.decolatech.viajava.domain.dtos.request.review.RatingCount(r.rating, COUNT(r)) " +
            "FROM Review r WHERE r.booking.travelPackage.id = :packageId GROUP BY r.rating")
    List<RatingCount> findRatingSummaryByPackageId(@Param("packageId") UUID packageId);
}