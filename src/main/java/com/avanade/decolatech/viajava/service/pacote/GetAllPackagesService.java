package com.avanade.decolatech.viajava.service.pacote;

import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetAllPackagesService {
    private final PackageRepository packageRepository;

    public GetAllPackagesService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Transactional(readOnly = true)
    public Page<Package> execute(Integer page, Integer size) {
        return packageRepository.findAll(PageRequest.of(page, size));
    }
}