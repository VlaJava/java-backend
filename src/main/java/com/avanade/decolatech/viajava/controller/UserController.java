package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.controller.docs.UserControllerSwagger;
import com.avanade.decolatech.viajava.domain.dtos.request.user.CreateUserRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.user.ResendLinkRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.user.UpdateRoleRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.user.UploadImageRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.payment.PaymentUserResponse;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController implements UserControllerSwagger {

    private final CreateUserService createUserService;
    private final GetUserByIdService getUserByIdService;
    private final GetUsersService getAllUsersService;
    private final GetFilteredUsersService getFilteredUsersService;
    private final DeleteUserService deleteUserService;
    private final UpdateUserImageService updateUserImageService;
    private final GetUserImageService getUserImageService;
    private final GetUserPaymentsService getUserPaymentsService;
    private final UpdateUserRoleService updateUserRoleService;
    private final ReactivateUserAccountService reactivateUserAccountService;
    private final UserMapper userMapper;

    public UserController(CreateUserService createUserService, GetUserByIdService getUserByIdService, GetUsersService getAllUsersService, GetFilteredUsersService getFilteredUsersService, DeleteUserService deleteUserService, UpdateUserImageService updateUserImageService, GetUserImageService getUserImageService, GetUserPaymentsService getUserPaymentsService, UpdateUserRoleService updateUserRoleService, ReactivateUserAccountService reactivateUserAccountService, UserMapper userMapper) {
        this.createUserService = createUserService;
        this.getUserByIdService = getUserByIdService;
        this.getAllUsersService = getAllUsersService;
        this.getFilteredUsersService = getFilteredUsersService;
        this.deleteUserService = deleteUserService;
        this.updateUserImageService = updateUserImageService;
        this.getUserImageService = getUserImageService;
        this.getUserPaymentsService = getUserPaymentsService;
        this.updateUserRoleService = updateUserRoleService;
        this.reactivateUserAccountService = reactivateUserAccountService;

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
        Page<User> response = this.getAllUsersService.execute(page, size);

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

    @GetMapping("/payments")
    public ResponseEntity<PaginatedResponse<PaymentUserResponse>> getUserPayments(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size,
            @AuthenticationPrincipal User user) {
        Page<PaymentUserResponse> response = getUserPaymentsService.execute(user.getId(), page, size);

        return ResponseEntity
                .ok(this.userMapper.toPaginatedUserPaymentsResponse(response));
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

    @Override
    @PatchMapping("/reactivate")
    public ResponseEntity<Void> reactivateUserAccount(@RequestBody @Valid ResendLinkRequest request) {
        this.reactivateUserAccountService.execute(request.getEmail());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> uploadImage(@ModelAttribute UploadImageRequest request, @PathVariable("id") UUID id) throws IOException {
        Resource response = this.updateUserImageService.updateProfileImage(request.getFile(), id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(response);
    }

    @PatchMapping(path = "/role")
    public ResponseEntity<Void> updateRole(@RequestBody UpdateRoleRequest request) {
        this.updateUserRoleService.updateRole(request);
        return ResponseEntity.noContent().build();
    }
}
