package com.avanade.decolatech.viajava.service.pacote;

import com.avanade.decolatech.viajava.config.storage.StoragePort;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UpdatePackageImageService {

    private final PackageRepository packageRepository;
    private final ApplicationProperties properties;
    private final StoragePort storagePort;

    public UpdatePackageImageService(PackageRepository packageRepository, ApplicationProperties properties, StoragePort storagePort) {
        this.packageRepository = packageRepository;
        this.properties = properties;
        this.storagePort = storagePort;
    }

    @Transactional
    public Resource updatePackageImage(MultipartFile file, UUID id) throws IOException {
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new IllegalArgumentException("Only JPEG or PNG images are allowed");
        }

        Package travelPackage = this.packageRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("[%s updatePackageImage] - %s",
                                this.getClass().getName(),
                                "Package not found with id: " + id)));


        String fileUri = this.storagePort.saveImage(file, this.properties.getPkgImgUploadDir(), id);

        travelPackage.setImageUrl(fileUri);

        this.packageRepository.save(travelPackage);

        return new InputStreamResource(file.getInputStream());
    }
}
