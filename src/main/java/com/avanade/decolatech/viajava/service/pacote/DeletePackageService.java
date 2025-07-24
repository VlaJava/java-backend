package com.avanade.decolatech.viajava.service.pacote;

import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeletePackageService {
    private final PackageRepository repository;

    public DeletePackageService(PackageRepository repository) {
        this.repository = repository;
    }

    public void execute(UUID id) {
        repository.deleteById(id);
    }
}