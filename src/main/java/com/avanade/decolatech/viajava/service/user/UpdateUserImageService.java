package com.avanade.decolatech.viajava.service.user;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.utils.UserExceptionMessages;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UpdateUserImageService {

    private final UserRepository userRepository;
    private final ApplicationProperties properties;

    public UpdateUserImageService(UserRepository userRepository, ApplicationProperties properties) {
        this.userRepository = userRepository;
        this.properties = properties;
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


        Path filePath = this.saveLocalImage(file, id);

        Resource resource = new UrlResource(filePath.toUri());

        user.setImageUrl(filePath.toString());

        this.userRepository.save(user);

        return resource;
    }

    private Path saveLocalImage(MultipartFile file, UUID id) throws IOException {

        Path uploadPath = Paths.get(this.properties.getUserImageUploadDir());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(id.toString());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath;
    }

}
