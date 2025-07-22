package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.CreateUsuarioRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUsuarioResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedUsuarioResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.UsuarioResponse;
import com.avanade.decolatech.viajava.domain.mapper.UsuarioMapper;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.model.enums.UsuarioRole;
import com.avanade.decolatech.viajava.service.usuario.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final CreateUsuarioService createUsuarioService;
    private final GetUsuarioByIdService getUsuarioByIdService;
    private final GetAllUsuariosService getAllUsuariosService;
    private final DeleteUsuarioService deleteUsuarioService;
    private final UpdateUsuarioImagemService updateUsuarioImagemService;

    private final UsuarioMapper usuarioMapper;

    public UsuarioController(
            CreateUsuarioService createUsuarioService,
            GetUsuarioByIdService getUsuarioByIdService,
            GetAllUsuariosService getAllUsuariosService,
            DeleteUsuarioService deleteUsuarioService, UpdateUsuarioImagemService updateUsuarioImagemService,
            UsuarioMapper usuarioMapper) {
        this.createUsuarioService = createUsuarioService;
        this.getUsuarioByIdService = getUsuarioByIdService;
        this.getAllUsuariosService = getAllUsuariosService;
        this.deleteUsuarioService = deleteUsuarioService;
        this.updateUsuarioImagemService = updateUsuarioImagemService;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    public ResponseEntity<CreateUsuarioResponse> criarUsuario(@RequestBody @Valid CreateUsuarioRequest request) {
        CreateUsuarioResponse response = this.createUsuarioService.criarUsuario(request, UsuarioRole.CLIENTE);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin")
    public ResponseEntity<CreateUsuarioResponse> criarUsuarioAdmin(@RequestBody @Valid CreateUsuarioRequest request) {
        CreateUsuarioResponse response = this.createUsuarioService.criarUsuario(request, UsuarioRole.ADMIN);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable("id") UUID id) {
        Usuario response = this.getUsuarioByIdService.execute(id);

        return ResponseEntity.ok(usuarioMapper.toUsuarioResponse(response));
    }

    @GetMapping
    public ResponseEntity<PaginatedUsuarioResponse> getTodosUsuarios(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size
    ) {
        Page<Usuario> response = this.getAllUsuariosService.execute(page, size);

        return ResponseEntity.ok(usuarioMapper.toPaginatedUsuarioResponse(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable("id") UUID id) {
        this.deleteUsuarioService.execute(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/update-image")
    public ResponseEntity<Resource> uploadImage(@RequestParam("file") MultipartFile file, @PathVariable("id") UUID id) throws IOException {
        Resource response = this.updateUsuarioImagemService.updateProfileImage(file, id);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(response);
    }
}
