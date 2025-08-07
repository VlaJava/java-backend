package com.avanade.decolatech.viajava.service.geminiChat.avanildo;

import com.avanade.decolatech.viajava.domain.dtos.response.pacote.PackageResponse;
import com.avanade.decolatech.viajava.domain.mapper.PackageMapper;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GetAllPackageChatService {

    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;

    public GetAllPackageChatService(PackageRepository packageRepository, PackageMapper packageMapper) {
        this.packageRepository = packageRepository;
        this.packageMapper = packageMapper;
    }

    @Transactional(readOnly = true)
    public List<PackageResponse> getAllPackageChat() {

        List<Package> packages = packageRepository.findAll();

        return packages.stream()
                .map(packageMapper::toPackageResponse)
                .filter(PackageResponse::available)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PackageResponse findById(UUID id) {
        Package packageEntity = packageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pacote com ID " + id + " não encontrado."));
        return packageMapper.toPackageResponse(packageEntity);
    }
}
