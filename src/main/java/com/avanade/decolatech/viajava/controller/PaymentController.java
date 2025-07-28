package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.payment.CreatePaymentRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.payment.CreatePreferenceResponse;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.service.payment.CreatePaymentService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final CreatePaymentService createPaymentService;
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    public PaymentController(CreatePaymentService createPaymentService) {
        this.createPaymentService = createPaymentService;
    }

    @PostMapping("/preference")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<CreatePreferenceResponse> createPreference(
            @RequestBody @Valid CreatePaymentRequest createPaymentRequest,
            @AuthenticationPrincipal User user
            ) throws MPException, MPApiException {

        CreatePreferenceResponse response = this.createPaymentService.createPayment(createPaymentRequest.bookingId(), user.getId());

        this.LOGGER.info("Mercado Pago redirect url {}", response.redirectUrl());

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", response.redirectUrl())
                .body(response);
    }

    @GetMapping("/success")
    @PreAuthorize("permitAll()")
    public String success() {
        return "success";
    }

    @GetMapping("/fail")
    @PreAuthorize("permitAll()")
    public String fail() {
        return "fail";
    }

    @GetMapping("/pending")
    @PreAuthorize("permitAll()")
    public String pending() {
        return "pending";
    }
}
