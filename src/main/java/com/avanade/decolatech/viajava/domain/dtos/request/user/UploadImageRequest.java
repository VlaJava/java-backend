package com.avanade.decolatech.viajava.domain.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private MultipartFile file;
}

