package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.controller.docs.UserControllerSwagger;
import com.avanade.decolatech.viajava.domain.dtos.request.user.CreateUserRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.user.UpdateRoleRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.user.UploadImageRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.user.CreateUserResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.user.UserResponse;

import com.avanade.decolatech.viajava.domain.mapper.UserMapper;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.model.enums.UserRole;
import com.avanade.decolatech.viajava.service.user.*;

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


@RestController
@RequestMapping("/users")
public class UserController implements UserControllerSwagger {

    private final CreateUserService createUserService;
    private final GetUserByIdService getUserByIdService;
    private final GetUsersService getAllUsuariosService;
    private final GetFilteredUsersService getFilteredUsersService;
    private final DeleteUserService deleteUserService;
    private final UpdateUserImageService updateUserImageService;
    private final GetUserImageService getUserImageService;
    private final UpdateUserRoleService updateUserRoleService;
    private final UserMapper userMapper;

    public UserController(CreateUserService createUserService, GetUserByIdService getUserByIdService, GetUsersService getAllUsuariosService, GetFilteredUsersService getFilteredUsersService, DeleteUserService deleteUserService, UpdateUserImageService updateUserImageService, GetUserImageService getUserImageService, UpdateUserRoleService updateUserRoleService, UserMapper userMapper) {
        this.createUserService = createUserService;
        this.getUserByIdService = getUserByIdService;
        this.getAllUsuariosService = getAllUsuariosService;
        this.getFilteredUsersService = getFilteredUsersService;
        this.deleteUserService = deleteUserService;
        this.updateUserImageService = updateUserImageService;
        this.getUserImageService = getUserImageService;
        this.updateUserRoleService = updateUserRoleService;

        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = this.createUserService.createUser(request, UserRole.CLIENT);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") UUID id) {
        User response = this.getUserByIdService.execute(id);

        return ResponseEntity.ok(userMapper.toUserResponse(response));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") @PositiveOrZero Integer page, @RequestParam(defaultValue = "10") @PositiveOrZero Integer size) {
        Page<User> response = this.getAllUsuariosService.execute(page, size);

        return ResponseEntity.ok(userMapper.toPaginatedUsersResponse(response));
    }

    @GetMapping("/filter")
    public ResponseEntity<PaginatedResponse<UserResponse>> getAllUsersWithFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String document,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        var users = getFilteredUsersService.execute(name, email, document, page, size);
        return ResponseEntity.ok(userMapper.toPaginatedUsersResponse(users));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getUserImage(@PathVariable("id") UUID id) {
        Resource resource = this.getUserImageService.getImage(id.toString());

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {
        this.deleteUserService.execute(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> uploadImage(@ModelAttribute UploadImageRequest request, @PathVariable("id") UUID id) throws IOException {
        Resource response = this.updateUserImageService.updateProfileImage(request.getFile(), id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(response);
    }

    @PatchMapping(path = "/role")
    public ResponseEntity<Void> updateRole(@RequestBody UpdateRoleRequest request)  {
        this.updateUserRoleService.updateRole(request);
        return ResponseEntity.noContent().build();
    }



}
