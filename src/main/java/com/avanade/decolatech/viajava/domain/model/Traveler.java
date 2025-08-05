package com.avanade.decolatech.viajava.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "TB_TRAVELERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Traveler {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "BOOKING_ID", nullable = false)
    @JsonIgnore
    private Booking booking;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "DOCUMENT_NUMBER", nullable = false, length = 20)
    private String document;

    @Column(name = "BIRTHDATE", nullable = false)
    private LocalDate birthdate;

}
