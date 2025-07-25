package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.CreateUserRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.UploadImageRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUserResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedUserResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.UserResponse;
import com.avanade.decolatech.viajava.domain.exception.ApplicationException;
import com.avanade.decolatech.viajava.domain.mapper.UserMapper;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.model.enums.UserRole;
import com.avanade.decolatech.viajava.service.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;


@Tag(name = "Users", description = "Endpoints for CRUD operations like create, search, deletion and update of a user.")
@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserService createUserService;
    private final GetUserByIdService getUserByIdService;
    private final GetUsersService getAllUsuariosService;
    private final DeleteUserService deleteUserService;
    private final UpdateUserImageService updateUserImageService;
    private final GetUserImageService getUserImageService;
    private final UserMapper userMapper;

    public UserController(CreateUserService createUserService, GetUserByIdService getUserByIdService, GetUsersService getAllUsuariosService, DeleteUserService deleteUserService, UpdateUserImageService updateUserImageService, GetUserImageService getUserImageService, UserMapper userMapper) {
        this.createUserService = createUserService;
        this.getUserByIdService = getUserByIdService;
        this.getAllUsuariosService = getAllUsuariosService;
        this.deleteUserService = deleteUserService;
        this.updateUserImageService = updateUserImageService;
        this.getUserImageService = getUserImageService;

        this.userMapper = userMapper;
    }

    @Operation(summary = "Create a new user with client role.", description = "Resource to create a new user with role client.", responses = {@ApiResponse(responseCode = "201", description = "User created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))), @ApiResponse(responseCode = "400", description = "Credential already been used for another user.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "Invalid request body parameters.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))} )
    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = this.createUserService.createUser(request, UserRole.CLIENT);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Creates a new user with admin role.", description = "Resource to create a new user with admin role.", security = @SecurityRequirement(name = "security"), responses = {@ApiResponse(responseCode = "201", description = "Admin created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))), @ApiResponse(responseCode = "400", description = "Credential already used by another user.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "The data passed in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))} )
    @PostMapping("/admin")
    public ResponseEntity<CreateUserResponse> createAdminUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = this.createUserService.createUser(request, UserRole.ADMIN);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Fetch a user by id.", description = "Resource to fetch an existing user by ID.", security = @SecurityRequirement(name = "security"), responses = {@ApiResponse(responseCode = "200", description = "User returned successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))), @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "The id passed in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))} )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") UUID id) {
        User response = this.getUserByIdService.execute(id);

        return ResponseEntity.ok(userMapper.toUserResponse(response));
    }

    @Operation(summary = "Returns a paginated list of users.", description = "Resource to fetch a paginated list of users.", security = @SecurityRequirement(name = "security"), responses = {@ApiResponse(responseCode = "200", description = "Users returned successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class)))} )
    @GetMapping
    public ResponseEntity<PaginatedUserResponse> getAllUsers(@RequestParam(defaultValue = "0") @PositiveOrZero Integer page, @RequestParam(defaultValue = "10") @PositiveOrZero Integer size) {
        Page<User> response = this.getAllUsuariosService.execute(page, size);

        return ResponseEntity.ok(userMapper.toPaginatedUsersResponse(response));
    }

    @Operation(summary = "Get user profile image.", description = "Resource to obtain the user's profile image.", security = @SecurityRequirement(name = "security"), responses = {@ApiResponse(responseCode = "200", description = "Image returned successfully.", content = @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary"))), @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "The id passed in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))})
    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getUserImage(@PathVariable("id") UUID id) {
        Resource resource = this.getUserImageService.getImage(id.toString());

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    @Operation(summary = "Deletes a user.", description = "Resource to delete an existing user by ID.", security = @SecurityRequirement(name = "security"), responses = {@ApiResponse(responseCode = "204", description = "User deleted successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))), @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "The id passed in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))} )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {
        this.deleteUserService.execute(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates the user's profile image.", description = "Resource to update the user's profile image.", security = @SecurityRequirement(name = "security"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = UploadImageRequest.class))), responses = {@ApiResponse(responseCode = "200", description = "Image updated successfully.", content = @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary"))), @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "The id passed in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))} )
    @PatchMapping(path = "/{id}/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> uploadImage(@ModelAttribute UploadImageRequest request, @PathVariable("id") UUID id) throws IOException {
        Resource response = this.updateUserImageService.updateProfileImage(request.getFile(), id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(response);
    }

}
