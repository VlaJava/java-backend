package com.avanade.decolatech.viajava.domain.mapper;

import com.avanade.decolatech.viajava.domain.dtos.request.pacote.CreatePackageRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.pacote.UpdatePackageRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.pacote.CreatePackageResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.pacote.PackageResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.model.Package;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PackageMapper {
    default CreatePackageResponse toCreatePackageResponse(Package pacote) {
        return new CreatePackageResponse(
                pacote.getId(),
                pacote.getTitle(),
                pacote.getSource(),
                pacote.getDestination(),
                pacote.getDescription(),
                pacote.getImageUrl(),
                pacote.getPrice(),
                pacote.getTravelerLimit(),
                pacote.getStartDate(),
                pacote.getEndDate(),
                pacote.isAvailable()
        );
    }

    @Mapping(target = "id", ignore = true)
    Package toPackage(CreatePackageRequest request);

    default void updatePackageFromRequest(UpdatePackageRequest request, Package pacote) {
        pacote.setTitle(request.getTitle() != null ? request.getTitle() : pacote.getTitle());
        pacote.setSource(request.getSource() != null ? request.getSource() : pacote.getSource());
        pacote.setDestination(request.getDestination() != null ? request.getDestination() : pacote.getDestination());
        pacote.setDescription(request.getDescription() != null ? request.getDescription() : pacote.getDescription());
        pacote.setImageUrl(request.getImageUrl() != null ? request.getImageUrl() : pacote.getImageUrl());
        pacote.setPrice(request.getPrice() != null ? request.getPrice() : pacote.getPrice());
        pacote.setTravelerLimit(request.getTravelerLimit() != null ? request.getTravelerLimit() : pacote.getTravelerLimit());
        pacote.setStartDate(request.getStartDate() != null ? request.getStartDate() : pacote.getStartDate());
        pacote.setEndDate(request.getEndDate() != null ? request.getEndDate() : pacote.getEndDate());
        pacote.setAvailable(request.getAvailable() != null ? request.getAvailable() : pacote.isAvailable());
    }



    PackageResponse toPackageResponse(Package pacote);

    default PaginatedResponse<PackageResponse> toPaginatedPackageResponse(Page<Package> pacotes, boolean available) {
        List<PackageResponse> packageResponse = pacotes
                .getContent()
                .stream()
                .filter(pacote -> !available || pacote.isAvailable())
                .map(this::toPackageResponse)
                .toList();

        return new PaginatedResponse<>(
                packageResponse,
                pacotes.getNumber(),
                pacotes.getTotalElements(),
                pacotes.getTotalPages()
        );
    }
}