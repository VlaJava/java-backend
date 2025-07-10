package com.avanade.decolatech.viajava.domain.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class Handler {

    private final Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApplicationException> handleBusinessException(HttpServletRequest request, BusinessException exception) {
        LOGGER.error(exception.getClass().getName());

        String formattedMessage = exception
                .getMessage()
                .replaceAll("\\[.*?]\\s*-?\\s*", "")
                .trim();


        ApplicationException response = new ApplicationException(
                request,
                HttpStatus.BAD_REQUEST,
                formattedMessage);

        return ResponseEntity
                .status(response.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApplicationException> handleResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException exception) {
        LOGGER.error(exception.getMessage());
        String formattedMessage = exception
                .getMessage()
                .replaceAll("\\[.*?]\\s*-?\\s*", "")
                .trim();


        ApplicationException response = new ApplicationException(
                request,
                HttpStatus.NOT_FOUND,
                formattedMessage);

        return ResponseEntity
                .status(response.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationException> handleMethodArgumentNotValidException(
            HttpServletRequest request,
            MethodArgumentNotValidException ex) {
        LOGGER.error("[Validation Error] - {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        new ApplicationException(
                                request,
                                HttpStatus.UNPROCESSABLE_ENTITY,
                                "Validation error",
                                ex.getBindingResult()
                        )
                );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApplicationException> handleMethodArgumentTypeMismatchException(
            HttpServletRequest request,
            MethodArgumentTypeMismatchException ex) {
        LOGGER.error("[Validation Error] - {}", ex.getMessage());


        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        new ApplicationException(
                                request,
                                HttpStatus.UNPROCESSABLE_ENTITY,
                                ex.getMessage()
                        )
                );
    }
}
