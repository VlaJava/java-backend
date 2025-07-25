package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.*;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUserResponse;
import com.avanade.decolatech.viajava.domain.exception.ApplicationException;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.service.auth.AuthService;
import com.avanade.decolatech.viajava.service.account.AccountConfirmationService;
import com.avanade.decolatech.viajava.service.account.ResendLinkAccountConfirmationService;
import com.avanade.decolatech.viajava.service.user.ForgotPasswordService;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth", description = "Endpoints for managing the authentication flow in the application.")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AccountConfirmationService accountConfirmationService;
    private final ResendLinkAccountConfirmationService resendLinkAccountConfirmationService;
    private final AuthenticationManager authenticationManager;
    private final ForgotPasswordService forgotPasswordService;
    private final ApplicationProperties properties;

    public AuthController(AuthService authService, AccountConfirmationService accountConfirmationService, ResendLinkAccountConfirmationService resendLinkAccountConfirmationService, AuthenticationManager authenticationManager, ForgotPasswordService forgotPasswordService, ApplicationProperties properties) {
        this.authService = authService;
        this.accountConfirmationService = accountConfirmationService;
        this.resendLinkAccountConfirmationService = resendLinkAccountConfirmationService;
        this.authenticationManager = authenticationManager;
        this.forgotPasswordService = forgotPasswordService;
        this.properties = properties;
    }

    @Operation(summary = "Logs in and returns the access token", description = "Resource to authenticate in the application.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "401", description = "Email and password do not match any record in the system.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid login credentials.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
            })
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        var authentication = new UsernamePasswordAuthenticationToken(request.email(), request.senha());

        Authentication authenticated = this.authenticationManager.authenticate(authentication);

        User user = (User) authenticated.getPrincipal();

        LoginResponse response = this.authService.generateToken(user);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Activates the user account.", description = "Resource to confirm the user's account.",
            responses = {
                    @ApiResponse(responseCode = "302", description = "User confirmed successfully.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "User not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "410", description = "The ID passed in the request is invalid.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
            })
    @GetMapping("/signup/account-confirmation")
    public ResponseEntity<Void> confirmAccount(@RequestParam("token") @Valid AccountConfirmationRequest request) {
        this.accountConfirmationService.confirmAccount(request.getToken());

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", this.properties.getRedirectUrl())
                .build();
    }

    @Operation(summary = "Resends the account confirmation link.", description = "Resource to resend the confirmation link to the user.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User returned successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "422", description = "The ID passed in the request is invalid.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
            })
    @PostMapping("/signup/resend-activation-link")
    public ResponseEntity<Void> resendConfirmationLink(@RequestBody @Valid ResendLinkRequest request) {
        this.resendLinkAccountConfirmationService.execute(request.getEmail());

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Sends password reset email.", description = "Resource to send a password reset email to the user.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Email sent successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "422", description = "The email passed in the request is invalid.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
            })
    @PostMapping("/signup/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResendLinkRequest request) {
        this.forgotPasswordService.sendPasswordRecoveryEmail(request.getEmail());

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates the password.", description = "Resource to update the user's password.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Password updated successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "400", description = "New password and confirm new password must match.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "422", description = "The passwords provided are invalid.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
            })
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> confirmPasswordReset(
            @RequestParam("token") String token,
            @RequestBody @Valid ResetPasswordRequest request) {
        this.forgotPasswordService.changePassword(request, token);

        return ResponseEntity.noContent().build();
    }

}
