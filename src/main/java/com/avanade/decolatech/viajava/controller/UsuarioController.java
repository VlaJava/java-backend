package com.avanade.decolatech.viajava.controller;

import com.avanade.decolatech.viajava.domain.dtos.request.CreateUsuarioRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.CreateUsuarioResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.PaginatedUsuarioResponse;
import com.avanade.decolatech.viajava.domain.dtos.response.UsuarioResponse;
import com.avanade.decolatech.viajava.domain.mapper.UsuarioMapper;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.service.CreateUsuarioService;
import com.avanade.decolatech.viajava.service.DeleteUsuarioService;
import com.avanade.decolatech.viajava.service.GetAllUsuariosService;
import com.avanade.decolatech.viajava.service.GetUsuarioByIdService;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final CreateUsuarioService createUsuarioService;
    private final GetUsuarioByIdService getUsuarioByIdService;
    private final GetAllUsuariosService getAllUsuariosService;
    private final DeleteUsuarioService deleteUsuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(
            CreateUsuarioService createUsuarioService,
            GetUsuarioByIdService getUsuarioByIdService,
            GetAllUsuariosService getAllUsuariosService,
            DeleteUsuarioService deleteUsuarioService,
            UsuarioMapper usuarioMapper) {
        this.createUsuarioService = createUsuarioService;
        this.getUsuarioByIdService = getUsuarioByIdService;
        this.getAllUsuariosService = getAllUsuariosService;
        this.deleteUsuarioService = deleteUsuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    public ResponseEntity<CreateUsuarioResponse> criarUsuario(CreateUsuarioRequest request) {
        CreateUsuarioResponse response = this.createUsuarioService.criarUsuario(request);

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


}
