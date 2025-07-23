package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.*;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUsuarioResponse;
import com.avanade.decolatech.viajava.domain.exception.ApplicationException;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.service.auth.AuthService;

import com.avanade.decolatech.viajava.service.conta.AccountConfirmationService;
import com.avanade.decolatech.viajava.service.conta.ReenviarLinkConfirmacaoContaService;
import com.avanade.decolatech.viajava.service.usuario.ForgotPasswordService;
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

@Tag(name = "auth", description = "Endpoints para gerenciamento do fluxo de autenticação da aplicação.")
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

    @Operation(summary = "Realiza o login e retorna o token de acesso", description = "Recurso para fazer a autenticação na aplicação.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "401", description = "Email e senha não correspondem a um registro no sistema",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "403", description = "Credenciais de Login Inválidas",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
            })
    @PostMapping
    public ResponseEntity<LoginResponse> login (@RequestBody
    LoginRequest request) {
        var authentication = new UsernamePasswordAuthenticationToken(request.email(), request.senha());

        Authentication authenticated = this.authenticationManager.authenticate(authentication);

        Usuario user = (Usuario) authenticated.getPrincipal();

        LoginResponse response = this.authService.generateToken(user);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Realiza ativação da conta do usuário.", description = "Recurso para realizar a confirmação da conta do usuário.",
            responses = {
                    @ApiResponse(responseCode = "302", description = "Usuário confirmado com sucesso.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "410", description = "O id passado na requisição é inválido.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
            })
    @GetMapping("/signup/confirmar-conta")
    public ResponseEntity<Void> confirmarConta(@RequestParam("token") @Valid ConfirmarContaRequest request) {
        this.accountConfirmationService.confirmarConta(request.getToken());

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", this.properties.getRedirectUrl())
                .build();
    }

    @Operation(summary = "Reenvia o link de confirmação de conta.", description = "Recurso para reenviar o link de confirmação para o usuário.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário retornado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUsuarioResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "422", description = "O id passado na requisição é inválido.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
            })
    @PostMapping("/signup/reenviar-link")
    public ResponseEntity<Void> reenviarLink(@RequestBody @Valid ReenviarLinkRequest request) {
        this.reenviarLinkConfirmacaoContaService.execute(request.getEmail());

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Envia email de reset senha.", description = "Recurso para enviar um email de reset de senha para o usuário.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Email enviado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUsuarioResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "422", description = "O email passado na requisição é inválido.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
            })
    @PostMapping("/signup/resetar-senha")
    public ResponseEntity<Void> resetarSenha(@RequestBody @Valid ReenviarLinkRequest request) {
        this.forgotPasswordService.enviarEmailRecuperarSenha(request.getEmail());

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Atualiza a senha.", description = "Recurso para fazer a atualização da senha.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUsuarioResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "400", description = "nova senha e confirmar nova senha devem ser equivalentes.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
                    @ApiResponse(responseCode = "422", description = "As senhas informadas são inválidas.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
            })
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> confirmarResetSenha(
            @RequestParam("token") String token,
            @RequestBody @Valid ResetarSenhaRequest request) {
        this.forgotPasswordService.alterarSenha(request, token);

        return ResponseEntity.noContent().build();
    }

}
