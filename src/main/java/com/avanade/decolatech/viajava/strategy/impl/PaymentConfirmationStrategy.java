package com.avanade.decolatech.viajava.strategy.impl;

import com.avanade.decolatech.viajava.domain.model.*;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.strategy.EmailStrategy;
import com.avanade.decolatech.viajava.strategy.EmailType;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.context.i18n.LocaleContextHolder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentConfirmationStrategy implements EmailStrategy {

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentConfirmationStrategy.class);
    private final JavaMailSender mailSender;
    private final ApplicationProperties properties;
    private final TemplateEngine templateEngine;

    public PaymentConfirmationStrategy(JavaMailSender mailSender,
                                       ApplicationProperties properties,
                                       TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.properties = properties;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(User user, Object... args) throws MessagingException, UnsupportedEncodingException {
        PaymentEntity payment = (PaymentEntity) args[0];
        @SuppressWarnings("unchecked")
        List<Traveler> travelers = (List<Traveler>) args[1];
        @SuppressWarnings("unchecked")
        List<Document> docs =  args[2] instanceof List<?> ? (List<Document>) args[2] : List.of();
        Booking booking = (Booking) args[3];

        String numeroPedido = booking.getOrderNumber();
        BigDecimal totalReserva = booking.getTotalPrice();
        BigDecimal totalPago = payment.getAmount();
        BigDecimal valorUnitario = totalReserva.divide(BigDecimal.valueOf(travelers.size()), 2, RoundingMode.HALF_UP);
        BigDecimal taxas = totalPago.subtract(totalReserva);
        String numeroNotaFiscal = generateNotaFiscal();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(user.getEmail());
        email.setSubject("[ViaJava] Payment Confirmation");
        email.setFrom(new InternetAddress(properties.getMailUsername(), "no-reply"));

        Context emailBodyContext = new Context(LocaleContextHolder.getLocale());
        emailBodyContext.setVariable("name", user.getName());
        emailBodyContext.setVariable("reservationsLink", this.properties.getFrontendBookingsUrl());
        emailBodyContext.setVariable("currentYear", LocalDateTime.now().getYear());
        String htmlContent = templateEngine.process("payment-confirmation", emailBodyContext);
        email.setText(htmlContent, true);

        Context context = new Context(LocaleContextHolder.getLocale());
        context.setVariable("empresa", this.createCompanyData());
        context.setVariable("cliente", this.createCustomerData(user, docs));
        context.setVariable("pagamento", payment);
        context.setVariable("numeroPedido", numeroPedido);
        context.setVariable("numeroNotaFiscal", numeroNotaFiscal);
        context.setVariable("valorUnitario", valorUnitario);
        context.setVariable("valorTotalReserva", totalReserva);
        context.setVariable("valorTaxas", taxas);
        context.setVariable("valorFinalPago", totalPago);
        context.setVariable("currentYear", LocalDateTime.now().getYear());
        context.setVariable("reservationsLink", this.properties.getFrontendBookingsUrl());

        String pdfContent =  templateEngine.process("payment-receipt", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(pdfContent, outputStream);
        email.addAttachment(
                String.format("%s-%s.pdf",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
                        ,UUID.randomUUID()),
                new ByteArrayResource(outputStream.toByteArray())
        );

        mailSender.send(mimeMessage);
    }

    private Object createCompanyData() {
        return new Object() {
            public String getNome() { return "ViaJava"; }
            public String getCnpj() { return "12.345.678/0001-99"; }
            public String getEndereco() { return "Rua Exemplo, 123 - Centro - São Paulo/SP - CEP: 01234-567"; }
            public String getContato() { return "(11) 3333-4444 - corporate.viajava@gmail.com"; }
        };
    }

    private Object createCustomerData(User user, List<Document> docs) {
        String docNumber = docs.isEmpty() ? "000.000.000-00" : docs.getFirst().getDocumentNumber();
        return new Object() {
            public String getName() { return user.getName(); }
            public String getDocumento() { return docNumber; }
            public String getEmail() { return user.getEmail(); }
            public String getTelefone() { return user.getPhone() != null ? user.getPhone() : "Não informado"; }
        };
    }

    private String generateNotaFiscal() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(44);
        for (int i = 0; i < 44; i++) sb.append(random.nextInt(10));
        return sb.toString();
    }

    @Override
    public EmailType getEmailType() {
        return EmailType.PAYMENT_CONFIRMATION_EMAIL;
    }
}
