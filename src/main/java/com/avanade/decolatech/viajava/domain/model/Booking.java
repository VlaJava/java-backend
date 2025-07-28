package com.avanade.decolatech.viajava.domain.model;

import com.avanade.decolatech.viajava.domain.model.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_BOOKINGS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PACKAGE_ID", nullable = false)
    private Package travelPackage;

    @Column(name = "TOTAL_PRICE", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "BOOKING_DATE", nullable = false)
    private LocalDateTime bookingDate;

    @Column(name = "TRAVEL_DATE", nullable = false)
    private LocalDate travelDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "BOOKING_STATUS", nullable = false)
    private BookingStatus bookingStatus;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Traveler> travelers;

    @PrePersist
    public void prePersist() {
        if (bookingDate == null) {
            bookingDate = LocalDateTime.now();
        }
        if (bookingStatus == null) {
            bookingStatus = BookingStatus.PENDING;
        }
    }
}