package com.avanade.decolatech.viajava.strategy.impl;

import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.strategy.EmailStrategy;
import com.avanade.decolatech.viajava.strategy.EmailType;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

/**
 * Sends email for user with a link of Mercado Pago to pay for a booking.
 */
@Service
public class PaymentRequestStrategy implements EmailStrategy {
    private final JavaMailSender mailSender;
    private final ApplicationProperties properties;
    private final TemplateEngine templateEngine;

    public PaymentRequestStrategy(JavaMailSender mailSender, ApplicationProperties properties, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.properties = properties;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(User user, Object... args) throws MessagingException, UnsupportedEncodingException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String paymentLink = args[0].toString();

        email.setTo(user.getEmail());
        email.setSubject("[ViaJava] Link de Pagamento");
        email.setFrom(new InternetAddress(this.properties.getMailUsername(), "no-reply"));

        final Context context = new Context(LocaleContextHolder.getLocale());
        context.setVariable("nome", user.getName());
        context.setVariable("linkPagamento", paymentLink);
        context.setVariable("dataAtual", LocalDateTime.now().getYear());

        final String htmlContent = this.templateEngine.process("payment-request", context);
        email.setText(htmlContent, true);

        this.mailSender.send(mimeMessage);
    }

    @Override
    public EmailType getEmailType() {
        return EmailType.PAYMENT_REQUEST_EMAIL;
    }
}
