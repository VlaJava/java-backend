package com.avanade.decolatech.viajava.service.user;

import com.avanade.decolatech.viajava.config.storage.StoragePort;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.utils.properties.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetUserImageService {

    private final StoragePort storagePort;

    public GetUserImageService(StoragePort storagePort) {
        this.storagePort = storagePort;

    }

    public Resource getImage(String path) {
        return this.storagePort.getImage(path);
    }
}

