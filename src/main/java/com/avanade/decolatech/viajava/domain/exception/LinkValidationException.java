package com.avanade.decolatech.viajava.domain.exception;

public class LinkValidationException extends RuntimeException {
    public LinkValidationException(String message) {
        super(message);
    }

    public LinkValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
