package com.avanade.decolatech.viajava.domain.dtos.request;

import com.avanade.decolatech.viajava.domain.model.enums.TipoDocumento;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateUsuarioRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    String nome;

    @NotNull
    @Email
    String email;

    @NotBlank
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "A senha deve conter letras maiúsculas, minúsculas e números")
    String senha;

    @NotBlank
    @Size(min = 11, max = 11)
    String telefone;

    @NotBlank
    @Size(min = 11, max = 20)
    String numeroDocumento;

    @NotBlank
    @Pattern(regexp = "CPF|PASSAPORTE")
    TipoDocumento tipoDocumento;
    
}
