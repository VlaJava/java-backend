package com.avanade.decolatech.viajava.service.email;

import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.strategy.EmailType;
import com.avanade.decolatech.viajava.strategy.factory.EmailFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private final EmailFactory emailFactory;
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(EmailFactory emailFactory) {
        this.emailFactory = emailFactory;
    }

    @Async("virtualThreadExecutor")
    public void sendEmail(User user, EmailType emailType, Object... args) {
        try {
            emailFactory.getStrategy(emailType.name()).sendEmail(user, args);
        } catch (Exception e) {
            logger.error("[{} sendEmail] - Email send failed to user {}. Error: {}", getClass().getSimpleName(), user.getEmail(), e.getMessage());
        }
    }
}
