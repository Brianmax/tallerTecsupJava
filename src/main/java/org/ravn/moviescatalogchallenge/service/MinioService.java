package org.ravn.moviescatalogchallenge.service;

import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    String uploadImage(MultipartFile file);
    String getPresignedUrl(String objectname);
    boolean deleteImage(String objectanme);
}
