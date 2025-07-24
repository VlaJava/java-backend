package com.avanade.decolatech.viajava.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "TB_PACKAGES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Column(name = "SOURCE", nullable = false, length = 100)
    private String source;

    @Column(name = "DESTINATION", nullable = false, length = 100)
    private String destination;

    @Column(name = "DESCRIPTION", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "IMAGE_URL", length = 255)
    private String imageUrl;

    @Column(name = "PRICE", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "TRAVELER_LIMIT", nullable = false)
    private int travelerLimit;

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDate endDate;

    @Column(name = "AVAILABLE", nullable = false)
    private boolean available;
}