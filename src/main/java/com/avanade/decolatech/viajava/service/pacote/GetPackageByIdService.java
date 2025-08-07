package com.avanade.decolatech.viajava.service.pacote;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetPackageByIdService {
    private final PackageRepository packageRepository;

    public GetPackageByIdService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Transactional(readOnly = true)
    public Package execute(UUID id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pacote não encontrado com o ID: " + id));
    }
}