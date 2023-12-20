package com.fordece.forum.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MinioService {

    /**
     * 保存图像到 minio
     *
     * @param images 从请求中获取的图像
     * @return java.util.List<java.lang.String> 对应的url
     * @author forDecember
     * @since 2023/12/20 16:50
     */
    List<String> saveImages(List<MultipartFile> images);

    /**
     * 保存视频到 minio
     *
     * @param videos 从请求中获取的视频
     * @return java.util.List<java.lang.String> 对应的url
     * @author forDecember
     * @since 2023/12/20 16:50
     */
    List<String> saveVideos(List<MultipartFile> videos);
}
