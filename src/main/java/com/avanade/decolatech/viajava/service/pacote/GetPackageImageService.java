package com.avanade.decolatech.viajava.service.pacote;

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
public class GetPackageImageService {

    private final ApplicationProperties properties;
    private final Logger logger = LoggerFactory.getLogger(GetPackageImageService.class);

    public GetPackageImageService(ApplicationProperties properties) {
        this.properties = properties;
    }

    public Resource getImage(String id) {
        Resource resource = null;

        try {
            Path filePath = Paths.get(this.properties.getPkgImgUploadDir()).resolve(id);

            resource = new UrlResource(filePath.toUri());

            if(!resource.exists()) {
                throw new ResourceNotFoundException("Image not found for this package");
            }
        } catch (MalformedURLException ex) {
            this.logger.error("{} getImage - MalformedURLException: {} ", this.getClass().getName(), ex.getMessage());

        }

        return resource;
    }
}
