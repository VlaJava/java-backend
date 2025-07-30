package com.avanade.decolatech.viajava.controller.docs;

import com.avanade.decolatech.viajava.domain.dtos.request.user.CreateUserRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.user.ResendLinkRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.user.UpdateRoleRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.user.UploadImageRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.payment.PaymentUserResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.user.CreateUserResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.user.UserResponse;
import com.avanade.decolatech.viajava.domain.exception.ApplicationException;
import com.avanade.decolatech.viajava.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@Tag(name = "Users", description = "Endpoints for CRUD operations like create, search, deletion and update of a user.")
public interface UserControllerSwagger {

    @Operation(summary = "Create a new user with client role.", description = "Resource to create a new user with role client.", responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Credential already been used for another user.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
            @ApiResponse(responseCode = "422", description = "Invalid request body parameters.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
    })
    ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request);

    @Operation(summary = "Fetch a user by id.", description = "Resource to fetch an existing user by ID.", security = @SecurityRequirement(name = "security"), responses = {
            @ApiResponse(responseCode = "200", description = "User returned successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
            @ApiResponse(responseCode = "422", description = "The id passed in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
    })
    ResponseEntity<UserResponse> getUserById(@PathVariable("id") UUID id);

    @Operation(summary = "Returns a paginated list of users.", description = "Resource to fetch a paginated list of users.", security = @SecurityRequirement(name = "security"), responses = {
            @ApiResponse(responseCode = "200", description = "Users returned successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    })
    ResponseEntity<PaginatedResponse<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") @PositiveOrZero Integer page, @RequestParam(defaultValue = "10") @PositiveOrZero Integer size);

    @Operation(summary = "Filter users with pagination", description = "Returns a paginated list of filtered users.", security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users filtered successfully", content = @Content(schema = @Schema(implementation = PaginatedResponse.class)))
    })
    ResponseEntity<PaginatedResponse<UserResponse>> getAllUsersWithFilter(
            @Parameter(description = "name", example = "Gustavo") @RequestParam(required = false) String name,
            @Parameter(description = "email", example = "gus@email.com") @RequestParam(required = false) String email,
            @Parameter(description = "document", example = "12340954") @RequestParam(required = false) String document,
            @Parameter(description = "page number", example = "0") @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @Parameter(description = "page size", example = "5") @RequestParam(defaultValue = "5") @PositiveOrZero Integer size
    );

    @Operation(summary = "Get user profile image.", description = "Resource to obtain the user's profile image.", security = @SecurityRequirement(name = "security"), responses = {
            @ApiResponse(responseCode = "200", description = "Image returned successfully.", content = @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
            @ApiResponse(responseCode = "422", description = "The id passed in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
    })
    ResponseEntity<Resource> getUserImage(@PathVariable("id") UUID id);

    @Operation(summary = "Filter user payments with pagination",
            description = "Returns a paginated list of filtered user payments.",
            security = @SecurityRequirement(name = "security"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users filtered successfully",
                    content = @Content(
                            schema = @Schema(implementation = PaginatedResponse.class))),
            @ApiResponse(responseCode = "403",
            description = "User not authenticated.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
    })
    ResponseEntity<PaginatedResponse<PaymentUserResponse>> getUserPayments(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size,
            @AuthenticationPrincipal User user);

    @Operation(summary = "Deletes a user.", description = "Resource to delete an existing user by ID.", security = @SecurityRequirement(name = "security"), responses = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
            @ApiResponse(responseCode = "422", description = "The id passed in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
    })
    ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id);

    @Operation(summary = "Reactivates the user account",
    description = "Resource to reactivate the user account",
    security = @SecurityRequirement(name = "security"),
    responses = {
            @ApiResponse(responseCode = "204", description = "User reactivated successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
            @ApiResponse(responseCode = "422", description = "The email provided in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
    })
    ResponseEntity<Void> reactivateUserAccount(@RequestBody @Valid ResendLinkRequest request);

    @Operation(summary = "Updates the user's profile image.", description = "Resource to update the user's profile image.", security = @SecurityRequirement(name = "security"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = UploadImageRequest.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Image updated successfully.", content = @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
            @ApiResponse(responseCode = "422", description = "The id passed in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
    })
    ResponseEntity<Resource> uploadImage(@ModelAttribute @Valid UploadImageRequest request, @PathVariable("id") UUID id) throws IOException;

    @Operation(summary = "Updates the user role.", description = "Resource to update the user role.", security = @SecurityRequirement(name = "security"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateRoleRequest.class))), responses = {
            @ApiResponse(responseCode = "204", description = "Role updated successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))),
            @ApiResponse(responseCode = "422", description = "The id passed in the request is invalid.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))
    })
    ResponseEntity<Void> updateRole(@RequestBody UpdateRoleRequest request);
}
