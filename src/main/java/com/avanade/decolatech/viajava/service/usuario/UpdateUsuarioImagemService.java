package com.avanade.decolatech.viajava.service.usuario;

import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import com.avanade.decolatech.viajava.utils.UsuarioExceptionMessages;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UpdateUsuarioImagemService {

    private final UsuarioRepository usuarioRepository;
    private final ApplicationProperties properties;

    public UpdateUsuarioImagemService(UsuarioRepository usuarioRepository, ApplicationProperties properties) {
        this.usuarioRepository = usuarioRepository;
        this.properties = properties;
    }

    @Transactional
    public Resource updateProfileImage(MultipartFile file, UUID id) throws IOException {
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new IllegalArgumentException("Only JPEG or PNG images are allowed");
        }

        Usuario usuario = this.usuarioRepository
                .findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException(
                        String.format("[%s uploadImage] - %s",
                                UpdateUsuarioImagemService.class.getName(),
                                UsuarioExceptionMessages.USUARIO_NAO_EXISTE)));


        Path filePath = this.saveLocalImage(file, id);

        Resource resource = new UrlResource(filePath.toUri());

        usuario.setImagemPerfil(filePath.toString());

        this.usuarioRepository.save(usuario);

        return resource;
    }

    private Path saveLocalImage(MultipartFile file, UUID id) throws IOException {

        Path uploadPath = Paths.get(this.properties.getUsuarioImgUploadDir());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(id.toString());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath;
    }

}
