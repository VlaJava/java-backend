package com.avanade.decolatech.viajava.service.pacote;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import com.avanade.decolatech.viajava.service.user.UpdateUserImageService;
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
public class UpdatePackageImageService {

    private final PackageRepository packageRepository;
    private final ApplicationProperties properties;

    public UpdatePackageImageService(PackageRepository packageRepository, ApplicationProperties properties) {
        this.packageRepository = packageRepository;
        this.properties = properties;
    }

    @Transactional
    public Resource updatePackageImage(MultipartFile file, UUID id) throws IOException {
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new IllegalArgumentException("Only JPEG or PNG images are allowed");
        }

        Package travelPackage = this.packageRepository
                .findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException(
                        String.format("[%s updatePackageImage] - %s",
                                this.getClass().getName(),
                                "Package not found with id: " + id)));


        Path filePath = this.saveLocalImage(file, id);

        Resource resource = new UrlResource(filePath.toUri());

        travelPackage.setImageUrl(filePath.toString());

        this.packageRepository.save(travelPackage);

        return resource;
    }

    private Path saveLocalImage(MultipartFile file, UUID id) throws IOException {

        Path uploadPath = Paths.get(this.properties.getPkgImgUploadDir());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(id.toString());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath;
    }

}
