package com.avanade.decolatech.viajava.domain.repository;

import com.avanade.decolatech.viajava.domain.dtos.response.view.GeneralMetricsView;
import com.avanade.decolatech.viajava.domain.dtos.response.payment.PaymentUserResponse;
import com.avanade.decolatech.viajava.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String telefone);

    @Query("SELECT U FROM User U WHERE U.email = :username")
    UserDetails findByUsername(String username);

    @Query("SELECT new com.avanade.decolatech.viajava.domain.dtos.response.payment.PaymentUserResponse(" +
            "P.id, " +
            "P.paymentMethod, " +
            "P.amount, " +
            "P.paymentDate, " +
            "P.paymentStatus) " +
            "FROM User U " +
            "LEFT JOIN Booking B ON U.id = B.user.id " +
            "RIGHT JOIN PaymentEntity P ON B.id = P.booking.id " +
            "WHERE U.id = :userId")
    Page<PaymentUserResponse> findAllPaymentsByUserId(UUID userId, Pageable pageable);

    @Query(value = "SELECT " +
            "ACTIVE_CLIENTS AS activeClients, " +
            "CONFIRMED_BOOKINGS AS confirmedBookings, " +
            "TOTAL_REVENUE AS totalRevenue, " +
            "AVG_RATINGS AS avgRatings, " +
            "AVAILABLE_PACKAGES AS availablePackages " +
            "FROM vw_general_metrics",
            nativeQuery = true)
    GeneralMetricsView getGeneralMetrics();
}
