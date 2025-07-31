package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.controller.docs.PackageControllerSwagger;
import com.avanade.decolatech.viajava.domain.dtos.request.CreatePackageRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.UpdatePackageRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreatePackageResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PackageResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.mapper.PackageMapper;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.service.pacote.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Packages", description = "Operations related to travel packages")
@RestController
@RequestMapping("/packages")
public class PackageController implements PackageControllerSwagger {

    private final CreatePackageService createPackageService;
    private final GetPackageByIdService getPackageByIdService;
    private final GetAllPackagesService getAllPackagesService;
    private final UpdatePackageService updatePackageService;
    private final DeletePackageService deletePackageService;
    private final GetFilterPackagesService getFilterPackagesService;
    private final PackageMapper packageMapper;

    public PackageController(
            CreatePackageService createPackageService,
            GetPackageByIdService getPackageByIdService,
            GetAllPackagesService getPackageAllService,
            UpdatePackageService updatePackageService,
            DeletePackageService deletePackageService,
            GetFilterPackagesService getFilterPackagesService,
            PackageMapper packageMapper
    ) {
        this.createPackageService = createPackageService;
        this.getPackageByIdService = getPackageByIdService;
        this.getAllPackagesService = getPackageAllService;
        this.updatePackageService = updatePackageService;
        this.deletePackageService = deletePackageService;
        this.getFilterPackagesService = getFilterPackagesService;

        this.packageMapper = packageMapper;
    }

    @PostMapping
    public ResponseEntity<CreatePackageResponse> createPackage(@RequestBody @Valid CreatePackageRequest request) {

        CreatePackageResponse response = this.createPackageService.criarPacote(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<PaginatedResponse<PackageResponse>> getFilterPackages(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size
    ) {
        Page<Package> response = getFilterPackagesService.execute(
                source, destination, startDate, endDate, price, page, size
        );
        return ResponseEntity.ok(packageMapper.toPaginatedPackageResponse(response, true));
    }

    @GetMapping
    public ResponseEntity< PaginatedResponse<PackageResponse> > getAllPackage(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size
    ) {
        Page<Package> response = this.getAllPackagesService.execute(page, size);

        return ResponseEntity.ok(packageMapper.toPaginatedPackageResponse(response, false));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageResponse> getByPackageId(@PathVariable UUID id) {
        Package pacote = this.getPackageByIdService.execute(id);

        return ResponseEntity.ok(packageMapper.toPackageResponse(pacote));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageResponse> updatePackage(
            @PathVariable UUID id,
            @RequestBody @Valid UpdatePackageRequest request
    ) {
        PackageResponse response = updatePackageService.execute(id, request);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable UUID id) {
        deletePackageService.execute(id);
        return ResponseEntity.noContent().build();
    }
}