package com.avanade.decolatech.viajava.domain.repository;

import com.avanade.decolatech.viajava.domain.dtos.response.view.MonthlyRevenueView;
import com.avanade.decolatech.viajava.domain.dtos.response.view.PendingBookingClientsView;
import com.avanade.decolatech.viajava.domain.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Page<Booking> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Booking> findByOrderNumber(String orderNumber);

    @Query("SELECT B FROM Booking B WHERE B.travelPackage.id = :packageId AND B.user.id = :userId")
    Optional<Booking> findByPackageAndUserId(UUID packageId, UUID userId);

    @Query(value = "SELECT " +
            "YEAR AS year, " +
            "MONTH AS month, " +
            "BOOKING_COUNT AS bookingCount, " +
            "REVENUE AS revenue " +
            "FROM vw_monthly_revenue",
            nativeQuery = true)
    List<MonthlyRevenueView> findMonthlyRevenue();

    @Query(value = "SELECT " +
            "CLIENT_NAME AS clientName, " +
            "EMAIL AS email, " +
            "PHONE AS phone, " +
            "BOOKING_ID AS bookingId, " +
            "TOTAL_PRICE AS totalPrice, " +
            "SALDO_DEVEDOR AS outstandingBalance " +
            "FROM vw_pending_booking_clients",
            nativeQuery = true)
    List<PendingBookingClientsView> findPendingBookingClients();
}
