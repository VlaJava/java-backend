package com.avanade.decolatech.viajava.service.account;


import com.avanade.decolatech.viajava.domain.exception.LinkValidationException;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.service.email.EmailService;
import com.avanade.decolatech.viajava.strategy.EmailType;
import com.avanade.decolatech.viajava.strategy.factory.EmailFactory;
import com.avanade.decolatech.viajava.utils.LinkExceptionMessages;
import com.avanade.decolatech.viajava.utils.TokenExceptionMessages;
import com.avanade.decolatech.viajava.utils.UserExceptionMessages;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AccountConfirmationService {

    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;
    private final EmailService emailService;

    public AccountConfirmationService(UserRepository userRepository, JwtDecoder jwtDecoder, EmailFactory emailFactory, EmailService emailService) {
        this.userRepository = userRepository;
        this.jwtDecoder = jwtDecoder;
        this.emailService = emailService;
    }

    @Transactional
    public void confirmAccount(String token) {
        Jwt extractedToken = this.jwtDecoder.decode(token);
        String email = extractedToken.getSubject();
        Instant expiration = extractedToken.getExpiresAt();
        String purpose = extractedToken.getClaimAsString("purpose");

        if (expiration != null && expiration.isBefore(Instant.now())) {
            throw new LinkValidationException(String.format("[%s confirmAccount] - %s",
                    LinkExceptionMessages.CONFIRMATION_LINK_EXPIRED,
                    AccountConfirmationService.class.getName()));
        }

        if (!purpose.equals("account_activation")) {
            throw new LinkValidationException(String.format("[%s confirmAccount] - %s.",
                    TokenExceptionMessages.INVALID_TOKEN_FOR_OPERATION,
                    AccountConfirmationService.class.getName()));
        }

        this.userRepository.findByEmail(email).ifPresentOrElse(user -> {
            user.setActive(true);
            this.userRepository.save(user);

            this.emailService.sendEmail(user, EmailType.ACCOUNT_CONFIRMATION_EMAIL);
        }, () -> {
            throw new ResourceNotFoundException(UserExceptionMessages.USER_NOT_FOUND);
        });
    }
}
