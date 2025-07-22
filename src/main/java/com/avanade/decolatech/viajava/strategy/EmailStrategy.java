package com.avanade.decolatech.viajava.strategy;

import com.avanade.decolatech.viajava.domain.model.Usuario;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailStrategy {
    void sendEmail(Usuario usuario) throws MessagingException, UnsupportedEncodingException;

    EmailType getEmailType();
}
