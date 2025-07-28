package com.avanade.decolatech.viajava.service.payment;

import com.avanade.decolatech.viajava.client.MercadoPagoClient;
import com.avanade.decolatech.viajava.domain.dtos.request.payment.CreatePreferenceRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.payment.CreatePreferenceResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Booking;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.BookingRepository;
import com.avanade.decolatech.viajava.utils.PaymentExceptionMessages;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class CreatePaymentService {

    private final Logger logger = LoggerFactory.getLogger(CreatePaymentService.class);
    private final BookingRepository bookingRepository;
    private final MercadoPagoClient mercadoPagoClient;
    private final ApplicationProperties properties;

    public CreatePaymentService(BookingRepository bookingRepository, MercadoPagoClient mercadoPagoClient, ApplicationProperties properties) {
        this.bookingRepository = bookingRepository;
        this.mercadoPagoClient = mercadoPagoClient;
        this.properties = properties;
    }

    @Transactional
    public CreatePreferenceResponse createPayment(UUID bookingId, UUID userId) throws MPException, MPApiException {
        Booking booking = this.bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        User user = booking.getUser();

        this.validateIfUserIsActive(user);
        this.validateIfPayerIsValid(userId, user.getId());

        Package travelPackage = booking.getTravelPackage();

        CreatePreferenceRequest request = this.buildCreatePreferenceRequest(user, travelPackage, booking);

        String orderNumber = this.generateOrderNumber(userId.toString());



        return this.mercadoPagoClient.createPreference(request, orderNumber);
    }

    /**
     * Validates if the payer is the same of the user that created the booking.
     * @param payerId id of the user that try to pay
     * @param bookingUserId id of the user saved in booking.
     * @throws BusinessException if Payer id is different of user that creates the booking.
     */
    private void validateIfPayerIsValid(UUID payerId, UUID bookingUserId) {
        if(!payerId.equals(bookingUserId)) {
            throw new BusinessException(PaymentExceptionMessages.PAYER_ID_DIFFERENT);
        }
    }

    private void validateIfUserIsActive(User user) {
        if(!user.isActive()) {
            throw new BusinessException(PaymentExceptionMessages.USER_IS_INACTIVE);
        }
    }

    private CreatePreferenceRequest buildCreatePreferenceRequest(User user, Package travelPackage, Booking booking) {
        var payerDTO = this.buildPayerDTO(user);

        var packageDTO = this.buildPackageDTO(travelPackage, booking.getTravelers().size());

        var backUrls = this.buildBackUrls();

        return CreatePreferenceRequest
                .builder()
                .payer(payerDTO)
                .backUrls(backUrls)
                .travelPackage(packageDTO)
                .build();
    }

    private CreatePreferenceRequest.PayerDTO buildPayerDTO(User user) {
        return new CreatePreferenceRequest.PayerDTO(user.getName(), user.getEmail());
    }

    private CreatePreferenceRequest.PackageDTO buildPackageDTO(Package travelPackage, int travelersNumber) {
       return new CreatePreferenceRequest.PackageDTO(travelPackage.getId(),
                travelPackage.getTitle(), travelPackage.getDescription(),
                travelersNumber, travelPackage.getPrice());
    }

    private CreatePreferenceRequest.BackUrlsDTO buildBackUrls() {
        this.logger.info("Success url {}", this.properties.getGatewaySuccessUrl());

        return new CreatePreferenceRequest.BackUrlsDTO(
                this.properties.getGatewaySuccessUrl(),
                this.properties.getGatewayFailUrl(),
                this.properties.getGatewayPendingUrl());
    }

    private String generateOrderNumber(String userId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
        String random = String.valueOf(System.currentTimeMillis() % 10000);
        return String.format("ORD-%s%s%s", userId, date, random);
    }
}
