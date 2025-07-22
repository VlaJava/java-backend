package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.*;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.service.auth.AuthService;

import com.avanade.decolatech.viajava.service.conta.AccountConfirmationService;
import com.avanade.decolatech.viajava.service.conta.ReenviarLinkConfirmacaoContaService;
import com.avanade.decolatech.viajava.service.usuario.ForgotPasswordService;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AccountConfirmationService accountConfirmationService;
    private final ReenviarLinkConfirmacaoContaService reenviarLinkConfirmacaoContaService;
    private final AuthenticationManager authenticationManager;
    private final ForgotPasswordService forgotPasswordService;
    private final ApplicationProperties properties;

    public AuthController(AuthService authService, AccountConfirmationService accountConfirmationService, ReenviarLinkConfirmacaoContaService reenviarLinkConfirmacaoContaService, AuthenticationManager authenticationManager, ForgotPasswordService forgotPasswordService, ApplicationProperties properties) {
        this.authService = authService;
        this.accountConfirmationService = accountConfirmationService;
        this.reenviarLinkConfirmacaoContaService = reenviarLinkConfirmacaoContaService;
        this.authenticationManager = authenticationManager;
        this.forgotPasswordService = forgotPasswordService;
        this.properties = properties;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login (@RequestBody
    LoginRequest request) {
        var authentication = new UsernamePasswordAuthenticationToken(request.email(), request.senha());

        Authentication authenticated = this.authenticationManager.authenticate(authentication);

        Usuario user = (Usuario) authenticated.getPrincipal();

        LoginResponse response = this.authService.generateToken(user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/signup/confirmar-conta")
    public ResponseEntity<Void> confirmarConta(@RequestParam("token") @Valid ConfirmarContaRequest request) {
        this.accountConfirmationService.confirmarConta(request.getToken());

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", this.properties.getRedirectUrl())
                .build();
    }

    @PostMapping("/signup/reenviar-link")
    public ResponseEntity<Void> reenviarLink(@RequestBody @Valid ReenviarLinkRequest request) {
        this.reenviarLinkConfirmacaoContaService.execute(request.getEmail());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signup/resetar-senha")
    public ResponseEntity<Void> resetarSenha(@RequestBody @Valid ReenviarLinkRequest request) {
        this.forgotPasswordService.enviarEmailRecuperarSenha(request.getEmail());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> confirmarResetSenha(
            @RequestParam("token") String token,
            @RequestBody @Valid ResetarSenhaRequest request) {
        this.forgotPasswordService.alterarSenha(request, token);

        return ResponseEntity.noContent().build();
    }

}
