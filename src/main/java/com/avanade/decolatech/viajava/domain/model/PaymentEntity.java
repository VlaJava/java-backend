package com.avanade.decolatech.viajava.domain.model;

import com.avanade.decolatech.viajava.domain.model.enums.DomainPaymentMethod;
import com.avanade.decolatech.viajava.domain.model.enums.DomainPaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_PAYMENTS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_ID", nullable = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(name = "METHOD", nullable = false)
    private DomainPaymentMethod paymentMethod;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "PAYMENT_DATE", nullable = false)
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS", nullable = false)
    private DomainPaymentStatus paymentStatus;
}
