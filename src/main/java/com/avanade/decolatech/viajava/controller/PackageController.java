package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.controller.docs.PackageControllerSwagger;
import com.avanade.decolatech.viajava.domain.dtos.request.pacote.CreatePackageRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.pacote.UpdatePackageRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.user.UploadImageRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.pacote.CreatePackageResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.pacote.PackageResponse;
import com.avanade.decolatech.viajava.domain.mapper.PackageMapper;
import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.service.pacote.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLConnection;
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
    private final GetPackageImageService getPackageImageService;
    private final UpdatePackageImageService updatePackageImageService;
    private final PackageMapper packageMapper;

    public PackageController(
            CreatePackageService createPackageService,
            GetPackageByIdService getPackageByIdService,
            GetAllPackagesService getPackageAllService,
            UpdatePackageService updatePackageService,
            DeletePackageService deletePackageService,
            GetFilterPackagesService getFilterPackagesService, GetPackageImageService getPackageImageService, UpdatePackageImageService updatePackageImageService,
            PackageMapper packageMapper
    ) {
        this.createPackageService = createPackageService;
        this.getPackageByIdService = getPackageByIdService;
        this.getAllPackagesService = getPackageAllService;
        this.updatePackageService = updatePackageService;
        this.deletePackageService = deletePackageService;
        this.getFilterPackagesService = getFilterPackagesService;
        this.getPackageImageService = getPackageImageService;
        this.updatePackageImageService = updatePackageImageService;

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
    public ResponseEntity<PaginatedResponse<PackageResponse>> getAllPackage(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size
    ) {
        Page<Package> response = this.getAllPackagesService.execute(page, size);

        return ResponseEntity.ok(packageMapper.toPaginatedPackageResponse(response, false));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getPackageImage(@PathVariable("id") UUID id) {
        Resource resource = this.getPackageImageService.getImage(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
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

    @PatchMapping(path = "/{id}/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> updateImage(@ModelAttribute UploadImageRequest request, @PathVariable("id") UUID id) throws IOException {
        MultipartFile file = request.getFile();
        String contentType = file.getContentType();

        Resource response = this.updatePackageImageService.updatePackageImage(request.getFile(), id);

        assert contentType != null;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getOriginalFilename() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable UUID id) {
        deletePackageService.execute(id);
        return ResponseEntity.noContent().build();
    }
}