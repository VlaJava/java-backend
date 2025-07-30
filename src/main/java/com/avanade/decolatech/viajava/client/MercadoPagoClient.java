package com.avanade.decolatech.viajava.client;

import com.avanade.decolatech.viajava.domain.dtos.request.payment.CreatePreferenceRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.payment.CreatePreferenceResponse;
import com.avanade.decolatech.viajava.domain.exception.PaymentGatewayException;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Component
public class MercadoPagoClient {

    private final ApplicationProperties properties;
    private final Logger LOGGER = LoggerFactory.getLogger(MercadoPagoClient.class);


    public MercadoPagoClient(ApplicationProperties properties) {
        this.properties = properties;

    }

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(this.properties.getGatewayAccessToken());
    }

    /**
     * Cria a preference dentro da SDK do Mercado Pago.
     *
     * @param request     request com os dados para criar o pedido (os dados vem da reserva no contexto da API ViaJava.)
     * @param orderNumber id do pedido que está armazenado no SDK do Mercado Pago.
     * @return CreatePreferenceResponse
     */
    @Async("virtualThreadExecutor")
    public CompletableFuture<CreatePreferenceResponse> createPreference(CreatePreferenceRequest request, String orderNumber)  {
        try {

            PreferenceClient preferenceClient = new PreferenceClient();

            CreatePreferenceRequest.PackageDTO travelPackage = request.travelPackage();

            List<PreferenceItemRequest> items =
                    List.of(PreferenceItemRequest
                            .builder()
                            .id(travelPackage.id().toString())
                            .title(travelPackage.title())
                            .quantity(travelPackage.quantityOfTravelers())
                            .unitPrice(travelPackage.unitPrice())
                            .build());

            PreferencePayerRequest payer = PreferencePayerRequest
                    .builder()
                    .name(request.payer().name())
                    .email(request.payer().email())
                    .build();

            PreferenceBackUrlsRequest backUrlsRequest = PreferenceBackUrlsRequest
                    .builder()
                    .success(request.backUrls().success())
                    .pending(request.backUrls().pending())
                    .failure(request.backUrls().failure())
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest
                    .builder()
                    .items(items)
                    .payer(payer)
                    .backUrls(backUrlsRequest)
                    .externalReference(orderNumber)
                    .autoReturn("approved")
                    .build();

            Preference preference = preferenceClient.create(preferenceRequest);

            var response = new CreatePreferenceResponse(
                    preference.getId(),
                    preference.getInitPoint());


            return CompletableFuture.completedFuture(response);

        } catch (MPApiException e) {
            throw new PaymentGatewayException(String.format(
                    "[MercadoPagoClient createPreference] - Mercado Pago Api Exception Occurs. Error Status: %s. Response: %s",
                    e.getStatusCode(),
                    e.getApiResponse().getContent()));
        } catch (MPException e) {
            throw new PaymentGatewayException(
                    "[MercadoPagoClient createPreference] - ", e);
        } catch (Exception e) {
            throw new PaymentGatewayException("Unexpected Error Occurs while try to create preference. ", e);
        }

    }

    public Payment getPaymentDetails(String paymentId) {
        try {

            PaymentClient paymentClient = new PaymentClient();

            Payment payment = paymentClient.get(Long.parseLong(paymentId));

            if (payment == null) {
                throw new PaymentGatewayException(String.format("Payment with id %s not found", paymentId));
            }

            return payment;
        } catch (MPApiException e) {
            throw new PaymentGatewayException("Mercado Pago Api Exception ", e);
        } catch (MPException e) {
            throw new PaymentGatewayException("Mercado Pago Exception ", e);
        } catch (Exception e) {
            throw new PaymentGatewayException("Unexpected Error Occurs while try to create preference. ", e);
        }
    }

}
