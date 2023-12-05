package com.fordece.forum.service;

import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface MinioService {
    public List<String> saveImages(List<MultipartFile> images);
}
