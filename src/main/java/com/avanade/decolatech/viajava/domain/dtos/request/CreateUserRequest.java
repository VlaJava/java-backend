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
public class CreateUserRequest {

    @NotBlank
    @Size(min = 3, max = 100)
   private String name;

    @NotNull
    @Email
   private String email;

    @NotBlank
    @Size(min = 8, message = "The password must have at least 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "the password must have at least one upper, one special character and number characters")
   private String password;

    @NotBlank
    @Size(min = 11, max = 11)
    private String phone;

    @NotBlank
    @Size(min = 8, max = 11, message = "The document number must have 11 characters if be cpf or 8 characters if be passport.")
    private String documentNumber;

    @NotBlank
    @Pattern(regexp = "CPF|PASSAPORTE", message = "The document type must be CPF or PASSAPORTE")
    private String documentType;


    @NotNull
    @Past
    private LocalDate birthDate;
}
