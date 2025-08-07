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
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class AccountCreatedStrategy implements EmailStrategy {

    private final ApplicationProperties properties;
    private final JavaMailSender mailSender;
    private final JwtEncoder jwtEncoder;
    private final TemplateEngine templateEngine;

    public AccountCreatedStrategy(ApplicationProperties properties, JavaMailSender mailSender, JwtEncoder jwtEncoder, TemplateEngine templateEngine) {
        this.properties = properties;
        this.mailSender = mailSender;
        this.jwtEncoder = jwtEncoder;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(User user, Object... args) throws MessagingException, UnsupportedEncodingException {
        String finalLink = this.getFinalLink(user.getEmail());

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(user.getEmail());
        email.setSubject("Seja bem vindo(a)");
        email.setFrom(new InternetAddress(this.properties.getMailUsername(), "viajava"));

        final Context context = new Context(LocaleContextHolder.getLocale());
        context.setVariable("nome", user.getName());
        context.setVariable("finalLink", finalLink);
        context.setVariable("dataAtual", LocalDateTime.now().getYear());

        final String htmlContent = this.templateEngine.process("registration", context);
        email.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    @Override
    public EmailType getEmailType() {
        return EmailType.ACCOUNT_CREATED_EMAIL;
    }

    private String getFinalLink(String email) {
        var claims = JwtClaimsSet.builder()
                .issuer("viajava-backend")
                .subject(email)
                .expiresAt(Instant.now().plusSeconds(7200))
                .claim("purpose", "account_activation")
                .build();

        String hashedContent = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return this.properties.getBaseUrl() + "/auth/signup/account-confirmation?token=" + hashedContent;
    }
}
