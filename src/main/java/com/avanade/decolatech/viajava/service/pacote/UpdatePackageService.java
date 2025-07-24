package com.avanade.decolatech.viajava.service.pacote;

import com.avanade.decolatech.viajava.domain.dtos.request.UpdatePacoteRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.PackageResponse;
import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.mapper.PackageMapper;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UpdatePackageService {
    private final PackageRepository repository;
    private final PackageMapper packageMapper;

    public UpdatePackageService(PackageRepository repository, PackageMapper packageMapper) {
        this.repository = repository;
        this.packageMapper = packageMapper;
    }

    @Transactional
    public PackageResponse execute(UUID id, UpdatePacoteRequest request) {
        Package existente = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Pacote não encontrado para o ID informado."));

        validarDatas(request.getDataInicio(), request.getDataFim());

        packageMapper.updatePacoteFromRequest(request, existente);

        repository.save(existente);

        return packageMapper.toPacoteResponse(existente);
    }

    private void validarDatas(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            throw new BusinessException("A data de fim do pacote não pode ser anterior à data de início.");
        }
    }
}