package com.avanade.decolatech.viajava.domain.repository;

import com.avanade.decolatech.viajava.domain.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Page<Booking> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Booking> findByOrderNumber(String orderNumber);

    @Query("SELECT B FROM Booking B WHERE B.travelPackage.id = :packageId AND B.user.id = :userId")
    Optional<Booking> findByPackageAndUserId(UUID packageId, UUID userId);
}
