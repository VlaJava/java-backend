package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.payment.CreatePaymentRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.payment.MercadoPagoNotification;
import com.avanade.decolatech.viajava.domain.dtos.request.payment.ProcessPaymentNotificationRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.payment.CreatePreferenceResponse;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.service.payment.CreatePreferenceService;
import com.avanade.decolatech.viajava.service.payment.ProcessPaymentNotificationService;
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

    private final CreatePreferenceService createPreferenceService;
    private final ProcessPaymentNotificationService notificationService;
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    public PaymentController(CreatePreferenceService createPreferenceService, ProcessPaymentNotificationService notificationService) {
        this.createPreferenceService = createPreferenceService;
        this.notificationService = notificationService;
    }

    @PostMapping("/preference")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> createPreference(
            @RequestBody @Valid CreatePaymentRequest createPaymentRequest,
            @AuthenticationPrincipal User user
    )  {

        this.createPreferenceService.createPayment(createPaymentRequest.bookingId(), user.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
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

    @PostMapping("/webhook")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> handleGatewayNotification(@RequestBody(required = false) MercadoPagoNotification notification) {
        if (notification == null || notification.getData() == null || notification.getData().getId() == null) {
            this.LOGGER.info("Received invalid gateway notification. Notification: {}", notification);
            return ResponseEntity.ok().build();
        }

        String resourceId = notification.getData().getId();
        String resourceType = notification.getType();

        if (!"payment".equalsIgnoreCase(resourceType)) {
            this.LOGGER.info("Non Payment Notification received: {}", resourceType);
            return ResponseEntity.ok().build();
        }

        var request = new ProcessPaymentNotificationRequest(resourceType, resourceId);

        try {

            this.notificationService.processNotification(request);

            return ResponseEntity.ok().build();
        } catch (Exception e) {

            return ResponseEntity.ok().build();
        }


    }
}
