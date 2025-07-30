package com.avanade.decolatech.viajava.domain.exception;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.jwt.JwtException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;



@RestControllerAdvice
public class Handler {

    private final Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApplicationException> handleBusinessException(HttpServletRequest request, BusinessException exception) {
        LOGGER.error(exception.getMessage());

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

    @ExceptionHandler(LinkValidationException.class)
    public ResponseEntity<ApplicationException> handleLinkValidationException(HttpServletRequest request, LinkValidationException exception) {
        LOGGER.error(exception.getMessage());

        String formattedMessage = exception
                .getMessage()
                .replaceAll("\\[.*?]\\s*-?\\s*", "")
                .trim();

        ApplicationException response = new ApplicationException(
                request,
                HttpStatus.GONE,
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApplicationException> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException exception) {
        LOGGER.error(exception.getMessage());

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

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApplicationException> handleDisabledException(HttpServletRequest request, DisabledException exception) {
        LOGGER.error(exception.getMessage());

        ApplicationException response = new ApplicationException(
                request,
                HttpStatus.FORBIDDEN,
                exception.getMessage());

        return ResponseEntity
                .status(response.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApplicationException> handleInvalidCredentialsException(HttpServletRequest request, InvalidCredentialsException exception) {
        LOGGER.error(exception.getMessage());

        String formattedMessage = exception
                .getMessage()
                .replaceAll("\\[.*?]\\s*-?\\s*", "")
                .trim();

        ApplicationException response = new ApplicationException(
                request,
                HttpStatus.UNAUTHORIZED,
                formattedMessage);

        return ResponseEntity
                .status(response.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApplicationException> handleBadCredentialsException(HttpServletRequest request, BadCredentialsException exception) {
        LOGGER.error("[Authentication Error] - {}", exception.getMessage());

        ApplicationException response = new ApplicationException(
                request,
                HttpStatus.UNAUTHORIZED,
                "Email e senha não correspondem ao registro no sistema");

        return ResponseEntity
                .status(response.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApplicationException> handleUsernameNotFoundException(HttpServletRequest request, UsernameNotFoundException exception) {
        LOGGER.error("[Authentication Error] - {}", exception.getMessage());

        ApplicationException response = new ApplicationException(
                request,
                HttpStatus.NOT_FOUND,
                "O email mencionado não existe no sistema");

        return ResponseEntity
                .status(response.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<ApplicationException> handleAuthenticationServiceException(HttpServletRequest request, AuthenticationServiceException exception) {
        LOGGER.error("[Authentication Service Error] - {}", exception.getMessage());

        ApplicationException response = new ApplicationException(
                request,
                HttpStatus.NOT_FOUND,
                exception.getMessage());

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApplicationException> handleHttpMessageNotReadableException(
            HttpServletRequest request,
            HttpMessageNotReadableException ex) {
        LOGGER.error("[Validation Error] - {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        new ApplicationException(
                                request,
                                HttpStatus.UNPROCESSABLE_ENTITY,
                                "Request body is required to perform this operation"
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

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApplicationException> handleJwtException(HttpServletRequest request, JwtException exception) {
        LOGGER.error("[JWT Error] - {}", exception.getMessage());

        ApplicationException response = new ApplicationException(
                request,
                HttpStatus.UNAUTHORIZED,
                "O token informado está inválido.");

        return ResponseEntity
                .status(response.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(PaymentGatewayException.class)
    public ResponseEntity<ApplicationException> handlePaymentGatewayException(HttpServletRequest request, PaymentGatewayException exception) {
        LOGGER.error("[PaymentGatewayException ] - {}", exception.getMessage());

        ApplicationException response = new ApplicationException(
                request,
                HttpStatus.BAD_REQUEST,
                "Payment process fail."
        );

        return ResponseEntity
                .status(response.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApplicationException> handleGenericException(HttpServletRequest request, Exception exception) {
        LOGGER.error("[class: {} ] - {}", exception.getClass() ,exception.getMessage());

        ApplicationException response = new ApplicationException(
                request,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro inesperado");

        return ResponseEntity
                .status(response.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}