package com.avanade.decolatech.viajava.strategy;

import com.avanade.decolatech.viajava.domain.model.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailStrategy {
    void sendEmail(User user, Object... args) throws MessagingException, UnsupportedEncodingException;

    EmailType getEmailType();
}
