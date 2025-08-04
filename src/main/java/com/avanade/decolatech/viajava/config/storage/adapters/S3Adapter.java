package com.avanade.decolatech.viajava.config.storage.adapters;

import com.avanade.decolatech.viajava.config.storage.StoragePort;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.utils.properties.ApplicationAwsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Profile("prod")
@Component
public class S3Adapter implements StoragePort {

    private final ApplicationAwsProperties properties;
    private final Logger logger = LoggerFactory.getLogger(S3Adapter.class);
    private final S3Client s3Client;

    public S3Adapter(ApplicationAwsProperties applicationAwsProperties, S3Client s3Client) {
        this.properties = applicationAwsProperties;
        this.s3Client = s3Client;
    }

    @Override
    public String saveImage(MultipartFile file, String path, UUID id) {
        String key = this.buildS3Key(file, path, id);
        try {
            byte[] fileBytes = file.getBytes();
            String contentType = file.getContentType();
            this.deletePreviousImageIfExists(key)
                    .thenCompose(v -> uploadFileToS3(fileBytes, contentType, key))
                    .exceptionally(ex -> {
                        logger.error("Error in upload", ex);
                        throw new RuntimeException(ex);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return key;
    }

    @Override
    public Resource getImage(String key) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(this.properties.getBucketName())
                    .key(key)
                    .build();

            return new InputStreamResource(s3Client.getObject(request));
        } catch (Exception e) {
            logger.error("Failed to load image from S3: {}", e.getMessage());
            throw new ResourceNotFoundException("Image not found or unreadable", e);
        }
    }

    @Override
    public void deleteImage(String key) {
        CompletableFuture.runAsync(() ->
        {
            DeleteObjectRequest request =
                    DeleteObjectRequest
                            .builder()
                            .bucket(this.properties.getBucketName())
                            .key(key)
                            .build();
            this.s3Client.deleteObject(request);
        });
    }

    private String buildS3Key(MultipartFile file, String path, UUID id) {
        String extension = extractExtension(file.getOriginalFilename());
        return String.format("%s/%s%s", path, id, extension);
    }

    private CompletableFuture<Void> deletePreviousImageIfExists(String key) {
        String bucketName = this.properties.getBucketName();
        String filePrefix = key.substring(0, key.indexOf("."));

        ListObjectsRequest listObjectsRequest = ListObjectsRequest
                .builder()
                .bucket(bucketName)
                .prefix(filePrefix)
                .build();

        return CompletableFuture.runAsync(() -> {
            var listResponse = s3Client.listObjects(listObjectsRequest);
            listResponse.contents().forEach(obj -> this.s3Client.deleteObject(DeleteObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(obj.key())
                    .build()));
        });
    }

    private CompletableFuture<Void> uploadFileToS3(byte[] fileBytes, String contentType, String key) {
        return CompletableFuture.runAsync(() -> {
            PutObjectRequest putObjectRequest = PutObjectRequest
                    .builder()
                    .bucket(this.properties.getBucketName())
                    .key(key)
                    .contentType(contentType)
                    .build();

            RequestBody body = RequestBody.fromBytes(fileBytes);
            s3Client.putObject(putObjectRequest, body);

        });

    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("File must have an extension.");
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
