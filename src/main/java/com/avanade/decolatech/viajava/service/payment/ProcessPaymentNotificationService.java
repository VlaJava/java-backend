package com.avanade.decolatech.viajava.service.payment;

import com.avanade.decolatech.viajava.client.MercadoPagoClient;
import com.avanade.decolatech.viajava.domain.dtos.request.payment.ProcessPaymentNotificationRequest;
import com.avanade.decolatech.viajava.domain.exception.PaymentGatewayException;
import com.avanade.decolatech.viajava.domain.model.Booking;
import com.avanade.decolatech.viajava.domain.model.PaymentEntity;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.model.enums.DomainPaymentMethod;
import com.avanade.decolatech.viajava.domain.model.enums.DomainPaymentStatus;
import com.avanade.decolatech.viajava.domain.repository.BookingRepository;
import com.avanade.decolatech.viajava.domain.repository.PaymentRepository;
import com.avanade.decolatech.viajava.service.email.EmailService;
import com.avanade.decolatech.viajava.strategy.EmailType;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentStatus;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessPaymentNotificationService {

    private final MercadoPagoClient mercadoPagoClient;
    private final Logger LOGGER = LoggerFactory.getLogger(ProcessPaymentNotificationService.class);
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final Map<String, DomainPaymentStatus> domainPaymentStatusMap = new HashMap<>();
    private final EmailService emailService;

    public ProcessPaymentNotificationService(MercadoPagoClient mercadoPagoClient, BookingRepository bookingRepository, PaymentRepository paymentRepository, EmailService emailService) {
        this.mercadoPagoClient = mercadoPagoClient;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.emailService = emailService;
        domainPaymentStatusMap.put(PaymentStatus.APPROVED, DomainPaymentStatus.APPROVED);
        domainPaymentStatusMap.put(PaymentStatus.PENDING, DomainPaymentStatus.PENDING);
        domainPaymentStatusMap.put(PaymentStatus.REJECTED, DomainPaymentStatus.REJECTED);
        domainPaymentStatusMap.put(PaymentStatus.AUTHORIZED, DomainPaymentStatus.AUTHORIZED);
        domainPaymentStatusMap.put(PaymentStatus.IN_PROCESS, DomainPaymentStatus.IN_PROCESS);
        domainPaymentStatusMap.put(PaymentStatus.IN_MEDIATION, DomainPaymentStatus.IN_MEDIATION);
        domainPaymentStatusMap.put(PaymentStatus.CANCELLED, DomainPaymentStatus.CANCELLED);
        domainPaymentStatusMap.put(PaymentStatus.REFUNDED, DomainPaymentStatus.REFUNDED);
        domainPaymentStatusMap.put(PaymentStatus.CHARGED_BACK, DomainPaymentStatus.CHARGED_BACK);
    }

    @Transactional
    public void processNotification(ProcessPaymentNotificationRequest request) {
        Payment payment = this.mercadoPagoClient.getPaymentDetails(request.getResourceId());

        String orderNumber = payment.getExternalReference();

        Booking booking = this.bookingRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow(() -> new PaymentGatewayException("Booking not found for order: " + orderNumber));

        DomainPaymentStatus status = this.domainPaymentStatusMap
                .getOrDefault(
                        payment.getStatus(),
                        DomainPaymentStatus.UNKNOWN);

        this.LOGGER.info("Processing notification with payment status: {}", status);
        this.LOGGER.info("Processing notification with order number: {}", orderNumber);

        DomainPaymentMethod paymentMethod = this.getPaymentMethod(payment.getPaymentTypeId());
        this.LOGGER.info("Processing notification with type: {}", paymentMethod);

        LocalDateTime paymentDate = payment.getDateApproved() != null ?
                payment.getDateApproved().toLocalDateTime() :
                payment.getDateCreated().toLocalDateTime();

        this.LOGGER.info("Creating payment...");

        PaymentEntity paymentEntity = PaymentEntity
                .builder()
                .booking(booking)
                .paymentMethod(paymentMethod)
                .paymentStatus(status)
                .amount(payment.getTransactionDetails().getTotalPaidAmount())
                .paymentDate(paymentDate)
                .build();

        this.LOGGER.info("Payment created successfully.");

        this.paymentRepository.save(paymentEntity);

        if(payment.getStatus().equals(PaymentStatus.APPROVED)) {
            User user = booking.getUser();

            Hibernate.initialize(user.getDocuments());
            Hibernate.initialize(booking.getTravelers());

            this.emailService.sendEmail(booking.getUser(), EmailType.PAYMENT_CONFIRMATION_EMAIL,
                    paymentEntity,
                    booking.getTravelers(),
                    user.getDocuments(),
                    booking);
        }
    }

    private DomainPaymentMethod getPaymentMethod(String paymentTypeId) {
        if (paymentTypeId == null || paymentTypeId.isBlank()) {
            return DomainPaymentMethod.OTHER;
        }

        return switch (paymentTypeId.toLowerCase()) {
            case "credit_card" -> DomainPaymentMethod.CREDIT;
            case "debit_card" -> DomainPaymentMethod.DEBIT;
            case "account_money" -> DomainPaymentMethod.ACCOUNT_MONEY;
            case "pix" -> DomainPaymentMethod.PIX;
            case "ticket" -> DomainPaymentMethod.BOLETO;
            default -> DomainPaymentMethod.OTHER;
        };
    }


}



