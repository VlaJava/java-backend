package com.avanade.decolatech.viajava.strategy.impl;

import com.avanade.decolatech.viajava.domain.model.Usuario;
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

@Service
public class AccountConfirmationStrategy implements EmailStrategy {
    private final JavaMailSender mailSender;
    private final ApplicationProperties properties;
    private final TemplateEngine templateEngine;

    public AccountConfirmationStrategy(JavaMailSender mailSender, ApplicationProperties properties, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.properties = properties;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(Usuario usuario) throws MessagingException, UnsupportedEncodingException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(usuario.getEmail());
        email.setSubject("[Conta Viajava] Confirmação de Conta");
        email.setFrom(new InternetAddress(this.properties.getMailUsername(), "no-reply"));

        final Context context = new Context(LocaleContextHolder.getLocale());
        context.setVariable("nome", usuario.getNome());
        context.setVariable("home", this.properties.getBaseUrl());
        context.setVariable("dataAtual", LocalDateTime.now().getYear());

        final String htmlContent = this.templateEngine.process("confirmation", context);
        email.setText(htmlContent, true);

        this.mailSender.send(mimeMessage);
    }

    @Override
    public EmailType getEmailType() {
        return EmailType.ACCOUNT_CONFIRMATION_EMAIL;
    }
}
