package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.CreateUserRequest;
import com.avanade.decolatech.viajava.domain.dtos.request.UploadImagemRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUsuarioResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedUsuarioResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.UsuarioResponse;
import com.avanade.decolatech.viajava.domain.exception.ApplicationException;
import com.avanade.decolatech.viajava.domain.mapper.UsuarioMapper;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.model.enums.UsuarioRole;
import com.avanade.decolatech.viajava.service.usuario.*;
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


@Tag(name = "Usuarios", description = "Endpoints para operações CRUD envolvendo criação, busca, deleção e atualização de um usuário.")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final CreateUsuarioService createUsuarioService;
    private final GetUsuarioByIdService getUsuarioByIdService;
    private final GetAllUsuariosService getAllUsuariosService;
    private final DeleteUsuarioService deleteUsuarioService;
    private final UpdateUsuarioImagemService updateUsuarioImagemService;
    private final GetUsuarioImagemService getUsuarioImagemService;

    private final UsuarioMapper usuarioMapper;

    public UsuarioController(CreateUsuarioService createUsuarioService, GetUsuarioByIdService getUsuarioByIdService, GetAllUsuariosService getAllUsuariosService, DeleteUsuarioService deleteUsuarioService, UpdateUsuarioImagemService updateUsuarioImagemService, GetUsuarioImagemService getUsuarioImagemService, UsuarioMapper usuarioMapper) {
        this.createUsuarioService = createUsuarioService;
        this.getUsuarioByIdService = getUsuarioByIdService;
        this.getAllUsuariosService = getAllUsuariosService;
        this.deleteUsuarioService = deleteUsuarioService;
        this.updateUsuarioImagemService = updateUsuarioImagemService;
        this.getUsuarioImagemService = getUsuarioImagemService;

        this.usuarioMapper = usuarioMapper;
    }

    @Operation(summary = "Cria um novo usuário com role cliente.", description = "Recurso para criar um novo usuário com role cliente.", responses = {@ApiResponse(responseCode = "201", description = "Usuário Criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUsuarioResponse.class))), @ApiResponse(responseCode = "400", description = "Credencial já utilizada por outro usuário.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "O dado passado na requisição é inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))})
    @PostMapping
    public ResponseEntity<CreateUsuarioResponse> criarUsuario(@RequestBody @Valid CreateUserRequest request) {
        CreateUsuarioResponse response = this.createUsuarioService.criarUsuario(request, UsuarioRole.CLIENTE);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Cria um novo usuário com role admin.", description = "Recurso para criar um novo usuário com role admin.", security = @SecurityRequirement(name = "security"), responses = {@ApiResponse(responseCode = "201", description = "Admin Criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUsuarioResponse.class))), @ApiResponse(responseCode = "400", description = "Credencial já utilizada por outro usuário.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "O dado passado na requisição é inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))})
    @PostMapping("/admin")
    public ResponseEntity<CreateUsuarioResponse> criarUsuarioAdmin(@RequestBody @Valid CreateUserRequest request) {
        CreateUsuarioResponse response = this.createUsuarioService.criarUsuario(request, UsuarioRole.ADMIN);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Busca um usuário pelo id.", description = "Recurso para buscar um novo usuário existente através do ID.", security = @SecurityRequirement(name = "security"), responses = {@ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUsuarioResponse.class))), @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "O id passado na requisição é inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable("id") UUID id) {
        Usuario response = this.getUsuarioByIdService.execute(id);

        return ResponseEntity.ok(usuarioMapper.toUsuarioResponse(response));
    }

    @Operation(summary = "Retorna uma lista de usuários paginados", description = "Recurso para buscar uma lista de usuários com paginação", security = @SecurityRequirement(name = "security"), responses = {@ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUsuarioResponse.class))),})
    @GetMapping
    public ResponseEntity<PaginatedUsuarioResponse> getTodosUsuarios(@RequestParam(defaultValue = "0") @PositiveOrZero Integer page, @RequestParam(defaultValue = "10") @PositiveOrZero Integer size) {
        Page<Usuario> response = this.getAllUsuariosService.execute(page, size);

        return ResponseEntity.ok(usuarioMapper.toPaginatedUsuarioResponse(response));
    }

    @Operation(summary = "Obtém a imagem de perfil do usuário.", description = "Recurso para obter a imagem de perfil do usuário.", security = @SecurityRequirement(name = "security"), responses = {@ApiResponse(responseCode = "200", description = "Imagem retornada com sucesso.", content = @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary"))), @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "O id passado na requisição é inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))})

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getUsuarioImagem(@PathVariable("id") UUID id) {
        Resource resource = this.getUsuarioImagemService.getImagem(id.toString());

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }


    @Operation(summary = "Deleta um usuário.", description = "Recurso para deletar um novo usuário existente através do ID.", security = @SecurityRequirement(name = "security"), responses = {@ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUsuarioResponse.class))), @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "O id passado na requisição é inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable("id") UUID id) {
        this.deleteUsuarioService.execute(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza a imagem de perfil do usuário.", description = "Recurso para atualizar a imagem de perfil do usuário.", security = @SecurityRequirement(name = "security"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = UploadImagemRequest.class))), responses = {@ApiResponse(responseCode = "200", description = "Imagem atualizada com sucesso.", content = @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary"))), @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class))), @ApiResponse(responseCode = "422", description = "O id passado na requisição é inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationException.class)))})
    @PatchMapping(path = "/{id}/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> uploadImage(@ModelAttribute UploadImagemRequest request, @PathVariable("id") UUID id) throws IOException {
        Resource response = this.updateUsuarioImagemService.updateProfileImage(request.getFile(), id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(response);
    }

}
