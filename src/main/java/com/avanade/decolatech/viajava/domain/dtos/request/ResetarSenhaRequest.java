package com.avanade.decolatech.viajava.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResetarSenhaRequest {

    @NotBlank
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    private String novaSenha;

    @NotBlank
    @Size(min = 8, message = "A confirmação de senha deve ter pelo menos 8 caracteres")
    private String novaSenhaConfirmacao;
}
