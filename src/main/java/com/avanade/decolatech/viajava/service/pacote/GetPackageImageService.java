package com.avanade.decolatech.viajava.service.pacote;

import com.avanade.decolatech.viajava.config.storage.StoragePort;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetPackageImageService {

    private final ApplicationProperties properties;
    private final StoragePort storagePort;
    private final PackageRepository packageRepository;

    public GetPackageImageService(ApplicationProperties properties, StoragePort storagePort, PackageRepository packageRepository) {
        this.properties = properties;
        this.storagePort = storagePort;
        this.packageRepository = packageRepository;
    }

    public Resource getImage(UUID id) {
       String path = this.packageRepository
               .findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Package  not found"))
               .getImageUrl();

       return this.storagePort.getImage(path);
    }
}
