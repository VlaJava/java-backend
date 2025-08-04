package com.avanade.decolatech.viajava.config.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface StoragePort {

    /**
     * @param file File to save
     * @param id   id of the entity that will be part of the name of file.
     * @param path path of the image
     * @return Path to file
     */
    String saveImage(MultipartFile file, String path, UUID id);

    Resource getImage(String key);

    void deleteImage(String key);
}
