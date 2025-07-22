package com.avanade.decolatech.viajava.domain.dtos.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateUsuarioRequest {

    @NotBlank
    @Size(min = 3, max = 100)
   private String nome;

    @NotNull
    @Email
   private String email;

    @NotBlank
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "A senha deve conter letras maiúsculas, minúsculas e números")
   private String senha;

    @NotBlank
    @Size(min = 11, max = 11)
    private String telefone;

    @NotBlank
    @Size(min = 8, max = 11, message = "O numero do documento deve ter 11 caracteres se for cpf ou 8 caracteres se for passaporte")
    private String numeroDocumento;

    @NotBlank
    @Pattern(regexp = "CPF|PASSAPORTE", message = "O tipo de documento deve ser CPF ou PASSAPORTE")
    private String tipoDocumento;


    @NotNull
    @Past
    private LocalDate dataNasc;
}
