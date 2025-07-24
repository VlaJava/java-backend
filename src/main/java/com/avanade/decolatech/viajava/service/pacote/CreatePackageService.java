package com.avanade.decolatech.viajava.service.pacote;

import com.avanade.decolatech.viajava.domain.dtos.request.CreatePacoteRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreatePackageResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.mapper.PackageMapper;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CreatePackageService {
    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;

    public CreatePackageService(PackageRepository packageRepository, PackageMapper packageMapper) {
        this.packageRepository = packageRepository;
        this.packageMapper = packageMapper;
    }

    @Transactional
    public CreatePackageResponse criarPacote(CreatePacoteRequest request) {
        Package Package = packageMapper.toPacote(request);

        this.validarDatas(Package);

        Package salvo = packageRepository.save(Package);

        return packageMapper.toCreatePackageResponse(salvo);
    }

    private void validarDatas(Package aPackage) {
        if (aPackage.getEndDate() != null && aPackage.getStartDate() != null && aPackage.getEndDate().isBefore(aPackage.getStartDate())) {
            throw new BusinessException("A data de fim do pacote não pode ser anterior à data de início.");
        }
    }
}