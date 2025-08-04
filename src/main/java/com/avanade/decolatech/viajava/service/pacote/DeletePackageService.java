package com.avanade.decolatech.viajava.service.pacote;

import com.avanade.decolatech.viajava.config.storage.StoragePort;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeletePackageService {
    private final PackageRepository repository;
    private final StoragePort storagePort;

    public DeletePackageService(PackageRepository repository, StoragePort storagePort) {
        this.repository = repository;
        this.storagePort = storagePort;
    }

    public void execute(UUID id) {
        this.repository
                .findById(id)
                .ifPresentOrElse(travelPackage -> {
                            String key = travelPackage.getImageUrl();
                            if (key != null) {
                                this.storagePort.deleteImage(key);
                            }
                            this.repository.delete(travelPackage);
                        },
                        () -> {
                            throw new ResourceNotFoundException("Package with id " + id + " not found.");
                        });
    }
}