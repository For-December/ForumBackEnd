package com.fordece.forum.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MinioService {
    List<String> saveImages(List<MultipartFile> images);
    List<String> saveVideos(List<MultipartFile> videos);
}
