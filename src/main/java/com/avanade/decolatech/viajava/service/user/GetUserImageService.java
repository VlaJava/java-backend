package com.avanade.decolatech.viajava.service.usuario;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class GetUsuarioImagemService {

    private final ApplicationProperties properties;
    private final Logger logger = LoggerFactory.getLogger(GetUsuarioImagemService.class);

    public GetUsuarioImagemService(ApplicationProperties properties) {
        this.properties = properties;
    }

    public Resource getImagem(String id) {
        Resource resource = null;

        try {
            Path filePath = Paths.get(this.properties.getUsuarioImgUploadDir()).resolve(id);

            resource = new UrlResource(filePath.toUri());

            if(!resource.exists()) {
                throw new ResourceNotFoundException("No profile picture image found for this user");
            }
        } catch (MalformedURLException ex) {
            this.logger.error("{} getImagem - MalformedURLException: {} ", GetUsuarioImagemService.class.getName(), ex.getMessage());

        }

        return resource;
    }
}

