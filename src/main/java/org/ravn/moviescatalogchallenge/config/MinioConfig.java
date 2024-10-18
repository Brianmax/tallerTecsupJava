package org.ravn.moviescatalogchallenge.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;


@Configuration
public class MinioConfig {
    Logger logger = Logger.getLogger(MinioConfig.class.getName());
    @Value("${minio.url}")
    String url;
    @Value("${minio.access-key}")
    String accessKey;
    @Value("${minio.secret-key}")
    String secretKey;
    @Value("${minio.bucket-name}")
    String bucketName;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                logger.info("Bucket '" + bucketName + "' creado exitosamente.");
            } else {
                logger.info("Bucket '" + bucketName + "' ya existe.");
            }
        } catch (Exception e) {
            logger.severe("Error al crear el bucket: " + e.getMessage());
        }
        return minioClient;
    }
}
