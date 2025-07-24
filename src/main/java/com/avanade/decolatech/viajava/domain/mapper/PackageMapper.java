package com.avanade.decolatech.viajava.domain.mapper;

import com.avanade.decolatech.viajava.domain.dtos.request.CreatePacoteRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.UpdatePacoteRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreatePackageResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PackageResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.model.Package;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PackageMapper {
    default CreatePackageResponse toCreatePackageResponse(Package aPackage) {
        return new CreatePackageResponse(
                aPackage.getId(),
                aPackage.getTitle(),
                aPackage.getSource(),
                aPackage.getDestination(),
                aPackage.getDescription(),
                aPackage.getImageUrl(),
                aPackage.getPrice(),
                aPackage.getTravelerLimit(),
                aPackage.getStartDate(),
                aPackage.getEndDate(),
                aPackage.isAvailable()
        );
    }

    @Mapping(target = "id", ignore = true)
    Package toPacote(CreatePacoteRequest request);

    default void updatePacoteFromRequest(UpdatePacoteRequest request, Package aPackage) {
        aPackage.setTitle(request.getTitulo());
        aPackage.setSource(request.getOrigem());
        aPackage.setDestination(request.getDestino());
        aPackage.setDescription(request.getDescricao());
        aPackage.setImageUrl(request.getImagemUrl());
        aPackage.setPrice(request.getValor());
        aPackage.setTravelerLimit(request.getLimiteViajantes());
        aPackage.setStartDate(request.getDataInicio());
        aPackage.setEndDate(request.getDataFim());
    }


    PackageResponse toPacoteResponse(Package aPackage);

    default PaginatedResponse<PackageResponse> toPaginatedPacoteResponse(Page<Package> pacotes) {
        List<PackageResponse> packageResponse =
                pacotes
                        .getContent()
                        .stream()
                        .filter(Package::isAvailable)
                        .map(this::toPacoteResponse)
                        .toList();

        return new PaginatedResponse<>(
                packageResponse,
                pacotes.getNumber(),
                pacotes.getTotalElements(),
                pacotes.getTotalPages()
        );
    }
}