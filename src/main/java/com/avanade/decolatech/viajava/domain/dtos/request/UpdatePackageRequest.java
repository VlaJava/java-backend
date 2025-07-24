package com.avanade.decolatech.viajava.domain.dtos.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePackageRequest {

    @Size(max = 100, message = "Title must have at most 100 characters.")
    private String title;

    @Size(max = 100, message = "Source must have at most 100 characters.")
    private String source;

    @Size(max = 100, message = "Destination must have at most 100 characters.")
    private String destination;

    private String description;

    private String imageUrl;

    @Positive(message = "Price must be greater than zero.")
    private BigDecimal price;

    @Min(value = 1, message = "Traveler limit must be at least 1.")
    private Integer travelerLimit;

    @FutureOrPresent(message = "Start date cannot be in the past.")
    private LocalDate startDate;

    @Future(message = "End date must be a future date.")
    private LocalDate endDate;

    private Boolean available;
}
