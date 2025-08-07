package com.avanade.decolatech.viajava.service.user;

import com.avanade.decolatech.viajava.config.storage.StoragePort;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.utils.UserExceptionMessages;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UpdateUserImageService {

    private final UserRepository userRepository;
    private final ApplicationProperties properties;
    private final StoragePort storagePort;

    public UpdateUserImageService(UserRepository userRepository, ApplicationProperties properties, StoragePort storagePort) {
        this.userRepository = userRepository;
        this.properties = properties;
        this.storagePort = storagePort;
    }

    @Transactional
    public Resource updateProfileImage(MultipartFile file, UUID id) throws IOException {
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new IllegalArgumentException("Only JPEG or PNG images are allowed");
        }

        User user = this.userRepository
                .findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException(
                        String.format("[%s uploadImage] - %s",
                                UpdateUserImageService.class.getName(),
                                UserExceptionMessages.USER_NOT_FOUND)));


        String fileUri = this.storagePort.saveImage(file, this.properties.getUserImageUploadDir() ,id);

        user.setImageUrl(fileUri);

        this.userRepository.save(user);

       return new InputStreamResource(file.getInputStream());
    }
}
