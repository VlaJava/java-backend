package com.avanade.decolatech.viajava.controller.docs;

import com.avanade.decolatech.viajava.domain.dtos.request.pacote.CreatePackageRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.pacote.UpdatePackageRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.user.UploadImageRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.pacote.CreatePackageResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.pacote.PackageResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.exception.ApplicationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

public interface PackageControllerSwagger {

    @Operation(summary = "Create a new package",
            description = "Creates a new package.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Package successfully created",
                content = @Content(schema = @Schema(implementation = CreatePackageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    ResponseEntity<CreatePackageResponse> createPackage(@RequestBody @Valid CreatePackageRequest request);

    @Operation(summary = "List all packages with pagination",
            description = "Returns a paginated list of all packages.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Packages listed successfully",
                content = @Content(schema = @Schema(implementation = PaginatedResponse.class)))
    })
    ResponseEntity<PaginatedResponse<PackageResponse>> getAllPackage(
            @Parameter(description = "Page", example = "0") @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @Parameter(description = "Page size", example = "6") @RequestParam(defaultValue = "6") @PositiveOrZero Integer size
    );

    @Operation(summary = "Filter packages with pagination",
            description = "Returns a paginated list of filtered packages.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Packages filtered successfully",
                content = @Content(schema = @Schema(implementation = PaginatedResponse.class)))
    })
    ResponseEntity<PaginatedResponse<PackageResponse>> getFilterPackages(
            @Parameter(description = "Source") @RequestParam(required = false) String source,
            @Parameter(description = "Destination") @RequestParam(required = false) String destination,
            @Parameter(description = "Start date", example = "2025-01-01") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
            @Parameter(description = "End date", example = "2025-12-31") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate endDate,
            @Parameter(description = "Max price") @RequestParam(required = false) java.math.BigDecimal price,
            @Parameter(description = "Page", example = "0") @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @Parameter(description = "Page size", example = "6") @RequestParam(defaultValue = "6") @PositiveOrZero Integer size
    );

    @Operation(summary = "Get a package by ID",
            description = "Returns a package by its ID.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Package found",
                content = @Content(schema = @Schema(implementation = PackageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Package not found", content = @Content)
    })
    ResponseEntity<PackageResponse> getByPackageId(@Parameter(description = "Package ID") @PathVariable UUID id);

    @Operation(summary = "Get Package Image",
    description = "Returns the package image",
    security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Package image found",
            content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE, schema = @Schema(type = "string", format = "binary"))),
    })
    ResponseEntity<Resource> getPackageImage(@PathVariable("id") UUID id);

    @Operation(summary = "Update an existing package",
            description = "Updates an existing package.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Package successfully updated",
                content = @Content(schema = @Schema(implementation = PackageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Package not found", content = @Content)
    })
    ResponseEntity<PackageResponse> updatePackage(
            @Parameter(description = "Package ID") @PathVariable UUID id,
            @RequestBody @Valid UpdatePackageRequest request
    );

    @Operation(summary = "Updates the package's image.", description = "Resource to update the package image.", security = @SecurityRequirement(name = "security"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = UploadImageRequest.class))), responses = {@ApiResponse(responseCode = "200", description = "Image updated successfully.", content = @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary"))), @ApiResponse(responseCode = "404", description = "Package not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "The id gived in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))} )
    ResponseEntity<Resource> updateImage(@ModelAttribute UploadImageRequest request, @PathVariable("id") UUID id) throws IOException;

    @Operation(summary = "Delete a package by ID",
            description = "Deletes a package by its ID.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Package successfully deleted", content = @Content),
        @ApiResponse(responseCode = "404", description = "Package not found", content = @Content)
    })
    ResponseEntity<Void> deletePackage(@Parameter(description = "Package ID") @PathVariable UUID id);
}
