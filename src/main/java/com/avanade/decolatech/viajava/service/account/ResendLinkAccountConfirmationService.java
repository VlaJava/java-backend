package com.avanade.decolatech.viajava.service.account;

import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.service.email.EmailService;
import com.avanade.decolatech.viajava.strategy.EmailType;
import com.avanade.decolatech.viajava.utils.UserExceptionMessages;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ResendLinkAccountConfirmationService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public ResendLinkAccountConfirmationService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional(readOnly = true)
    public void execute(String email) {
        User user = this.userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(UserExceptionMessages.USER_NOT_FOUND));

        if (user.isActive()) {
            throw new BusinessException(
                    String.format("[%s execute] - The account provided already have been activated.]",
                            ResendLinkAccountConfirmationService.class.getName()
                    ));
        }

        this.emailService.sendEmail(user, EmailType.RESEND_ACCOUNT_CONFIRMATION_EMAIL);

    }
}
