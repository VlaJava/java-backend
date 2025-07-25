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
public class ForgotPasswordStrategy implements EmailStrategy {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final JwtEncoder jwtEncoder;
    private final ApplicationProperties properties;

    public ForgotPasswordStrategy(JavaMailSender mailSender, TemplateEngine templateEngine, JwtEncoder jwtEncoder, ApplicationProperties properties) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.jwtEncoder = jwtEncoder;
        this.properties = properties;
    }

    @Override
    public void sendEmail(User user) throws MessagingException, UnsupportedEncodingException {
        String finalLink = this.getFinalLink(user.getEmail());
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(user.getEmail());
        email.setSubject("[Conta Viajava] Recuperação de Senha");
        email.setFrom(new InternetAddress(this.properties.getMailUsername(), "no-reply"));

        final Context context = new Context(LocaleContextHolder.getLocale());
        context.setVariable("nome", user.getName());
        context.setVariable("linkConfirmacao", finalLink);
        context.setVariable("dataAtual", LocalDateTime.now().getYear());

        final String htmlContent = this.templateEngine.process("password-recover", context);
        email.setText(htmlContent, true);

        this.mailSender.send(mimeMessage);
    }

    @Override
    public EmailType getEmailType() {
        return EmailType.PASSWORD_RESET_EMAIL;
    }

    public String getFinalLink(String email) {
        var claims = JwtClaimsSet
                .builder()
                .issuer("viajava-backend")
                .subject(email)
                .claim("purpose", "password_reset")
                .expiresAt(Instant.now().plusSeconds(900))
                .build();

        String hashedContent = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return this.properties.getBaseUrl() + "/auth/forgot-password?token=" + hashedContent;
    }
}
