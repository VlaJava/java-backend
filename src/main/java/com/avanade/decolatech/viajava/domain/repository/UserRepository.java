package com.avanade.decolatech.viajava.domain.repository;

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
            "LEFT JOIN Booking B ON U.id = B.user " +
            "RIGHT JOIN PaymentEntity P ON B.id = P.booking " +
            "WHERE U.id = :userId")
    Page<PaymentUserResponse> findAllPaymentsByUserId(UUID userId, Pageable pageable);
}
