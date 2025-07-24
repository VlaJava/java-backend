package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.CreatePacoteRequest;

import com.avanade.decolatech.viajava.domain.dtos.request.UpdatePacoteRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreatePackageResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PackageResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.mapper.PackageMapper;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.service.pacote.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/packages")
public class PackageController {

    private final CreatePackageService createPackageService;
    private final GetPackageByIdService getPacoteByIdService;
    private final GetAllPackagesService getAllPackagesService;
    private final UpdatePackageService updatePackageService;
    private final DeletePackageService deletePackageService;
    private final PackageMapper packageMapper;

    public PackageController(
            CreatePackageService createPackageService,
            GetPackageByIdService getPacoteByIdService,
            GetAllPackagesService getPacoteAllService,
            UpdatePackageService updatePackageService,
            DeletePackageService deletePackageService,
            PackageMapper packageMapper
    ) {
        this.createPackageService = createPackageService;
        this.getPacoteByIdService = getPacoteByIdService;
        this.getAllPackagesService = getPacoteAllService;
        this.updatePackageService = updatePackageService;
        this.deletePackageService = deletePackageService;

        this.packageMapper = packageMapper;
    }

    @PostMapping
    public ResponseEntity<CreatePackageResponse> createPackage(@RequestBody @Valid CreatePacoteRequest request) {

        CreatePackageResponse response = this.createPackageService.criarPacote(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity< PaginatedResponse<PackageResponse> > getAllPackage(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "6") @PositiveOrZero Integer size
    ) {
        Page<Package> response = this.getAllPackagesService.execute(page, size);

        return ResponseEntity.ok(packageMapper.toPaginatedPacoteResponse(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageResponse> getById(@PathVariable UUID id) {
        Package aPackage = this.getPacoteByIdService.execute(id);

        return ResponseEntity.ok(packageMapper.toPacoteResponse(aPackage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdatePacoteRequest request
    ) {
        PackageResponse response = updatePackageService.execute(id, request);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deletePackageService.execute(id);
        return ResponseEntity.noContent().build();
    }
}