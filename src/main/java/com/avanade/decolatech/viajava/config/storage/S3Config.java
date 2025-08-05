package com.avanade.decolatech.viajava.config.storage;

import com.avanade.decolatech.viajava.utils.properties.ApplicationAwsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Profile("prod")
@Configuration
public class S3Config {

    private final ApplicationAwsProperties properties;

    public S3Config(ApplicationAwsProperties properties) {
        this.properties = properties;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client
                .builder()
                .region(Region.of(this.properties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials()))
                .build();
    }

    public AwsCredentials credentials() {
        return AwsBasicCredentials.create(this.properties.getAccessKey(), this.properties.getSecretKey());
    }
}
