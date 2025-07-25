package com.avanade.decolatech.viajava.utils.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Validated
@Getter
@Component
public class ApplicationProperties {

    @Value("${jwt.public-key}")
    private RSAPublicKey publicKey;

    @Value("${jwt.private-key}")
    private RSAPrivateKey privateKey;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.redirect-url}")
    private String redirectUrl;

    @Value("${app.file.upload.user-dir}")
    private String userImageUploadDir;

    @Value("${app.file.upload.pkg-dir}")
    private String pkgImgUploadDir;

    @Value("${app.frontend-url}")
    private String frontendUrl;
}
