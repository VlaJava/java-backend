package com.avanade.decolatech.viajava.domain.dtos.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageRequest {
    @Schema(type = "string", format = "binary", description = "Arquivo de imagem JPEG ou PNG")
    @NotNull
    private MultipartFile file;
}

