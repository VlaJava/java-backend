package com.avanade.decolatech.viajava.service.user;

import com.avanade.decolatech.viajava.domain.dtos.request.ResetPasswordRequest;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.exception.LinkValidationException;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.service.email.EmailService;
import com.avanade.decolatech.viajava.strategy.EmailType;
import com.avanade.decolatech.viajava.utils.LinkExceptionMessages;
import com.avanade.decolatech.viajava.utils.TokenExceptionMessages;
import com.avanade.decolatech.viajava.utils.UserExceptionMessages;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtDecoder jwtDecoder;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordService(UserRepository userRepository, EmailService emailService, JwtDecoder jwtDecoder, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtDecoder = jwtDecoder;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendPasswordRecoveryEmail(String email) {
        this.userRepository.findByEmail(email).ifPresentOrElse(user -> {
                    this.emailService.sendEmail(user, EmailType.PASSWORD_RESET_EMAIL);
                },

                () -> {
                    throw new ResourceNotFoundException(UserExceptionMessages.USER_NOT_FOUND);
                });
    }

    public void changePassword(ResetPasswordRequest request, String token) {
        String newPassword = request.getPassword();
        String passwordConfirmation = request.getPasswordConfirmation();

        if (!newPassword.equals(passwordConfirmation)) {
            throw new BusinessException(String.format("[%s changePassword] -  The password must be equal to password confirmation.", ForgotPasswordService.class.getName()));
        }

        Jwt decodedToken = this.jwtDecoder.decode(token);
        String email = decodedToken.getSubject();
        String purpose = decodedToken.getClaimAsString("purpose");
        Instant expiration = decodedToken.getExpiresAt();

        if (expiration != null && expiration.isBefore(Instant.now())) {
            throw new LinkValidationException(String.format("[%s changePassword] -  %s.", LinkExceptionMessages.CONFIRMATION_LINK_EXPIRED,
                    ForgotPasswordService.class.getName()));
        }

        if (!purpose.equals("password_reset")) {
            throw new LinkValidationException(String.format("[%s changePassword] -  %s.",
                    TokenExceptionMessages.INVALID_TOKEN_FOR_OPERATION,
                    ForgotPasswordService.class.getName()));
        }

        this.userRepository.findByEmail(email).ifPresentOrElse(user -> {
            String newHashedPassword = this.passwordEncoder.encode(newPassword);
            user.setPassword(newHashedPassword);

            this.userRepository.save(user);
        }, () -> {
            throw new ResourceNotFoundException(UserExceptionMessages.USER_NOT_FOUND);
        });


    }
}
