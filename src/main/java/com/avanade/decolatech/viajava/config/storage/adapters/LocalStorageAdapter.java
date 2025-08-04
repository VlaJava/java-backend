package com.avanade.decolatech.viajava.config.storage.adapters;

import com.avanade.decolatech.viajava.config.storage.StoragePort;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.service.user.GetUserImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.*;
import java.util.UUID;

@Profile("default")
@Component
public class LocalStorageAdapter implements StoragePort {
    private final Logger logger = LoggerFactory.getLogger(LocalStorageAdapter.class);

    @Override
    public String saveImage(MultipartFile file, String path, UUID id) {
        try {
            Path uploadPath = createDirectoryIfNotExists(path);
            String fileName = generateFileName(file.getOriginalFilename(), id);
            Path filePath = uploadPath.resolve(fileName);

            saveFile(file, filePath);

            return filePath.toString();
        } catch (IOException e) {
            logger.error("[LocalStorageAdapter] saveImage: - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource getImage(String key) {
        try {
            Path filePath = Paths.get(key).toRealPath();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new ResourceNotFoundException("Image not found in this path.");
            }

            return resource;
        } catch (MalformedURLException ex) {
            logger.error("{} getImage - MalformedURLException: {}", GetUserImageService.class.getName(), ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        } catch (IOException e) {
            logger.error("{} getImage - IOException: {}", GetUserImageService.class.getName(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteImage(String key) {
        Path deletionPath = Paths.get(key);

        boolean fileNotExists = Files.notExists(deletionPath);

        if (fileNotExists) {
            return;
        }

        var isDeleted = deletionPath.toFile().delete();

        if(!isDeleted) {
            throw new ResourceNotFoundException("Image not found in this path.");
        }
    }

    private Path createDirectoryIfNotExists(String path) throws IOException {
        Path uploadPath = Paths.get(path);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        return uploadPath;
    }

    private String generateFileName(String originalFilename, UUID id) {
        String extension = extractExtension(originalFilename);
        return id + extension;
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("File must have an extension.");
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private void saveFile(MultipartFile file, Path destination) throws IOException {
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
    }
}
