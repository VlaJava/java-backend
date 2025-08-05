package com.avanade.decolatech.viajava.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "TB_REVIEWS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_ID", nullable = false)
    @JsonIgnore
    private Booking booking;

    @Column(name = "RATING")
    private Integer rating;

    @Column(name = "COMMENT", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "REVIEW_DATE", updatable = false)
    private LocalDate reviewDate;

    @Column(name = "REMOVED")
    private boolean removed = false;

    @PrePersist
    protected void onCreate() {
        reviewDate = LocalDate.now();
    }
}