package org.ravn.moviescatalogchallenge.service.impl;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.ravn.moviescatalogchallenge.service.MinioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;
    private final Logger logger = LoggerFactory.getLogger(MinioServiceImpl.class);
    public MinioServiceImpl(MinioClient minioClient)
    {
        this.minioClient = minioClient;
    }
    @Override
    public String uploadImage(MultipartFile file) {
        String objectname = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket("movies")
                    .object(objectname)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
        return objectname;
    }

    @Override
    public String getPresignedUrl(String objectname) {
        Map<String, String> reqParams = Map.of(
                "response-content-type", "application/json");
        new GetPresignedObjectUrlArgs();
        String message = "";
        if (objectname == null) {
            return  "The movie does not have an image";
        }
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs
                            .builder()
                            .method(Method.GET)
                            .bucket("movies")
                            .object(objectname)
                            .expiry(2, TimeUnit.HOURS)
                            .extraQueryParams(reqParams)
                            .build()
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Error getting presigned url " + e.getMessage();
        }
    }

    @Override
    public boolean deleteImage(String objectanme) {
        return false;
    }
}
