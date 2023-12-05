package com.fordece.forum.service.impl;

import com.fordece.forum.service.MinioService;
import com.fordece.forum.utils.Const;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MinioServiceImpl implements MinioService {
    @Resource
    MinioClient minioClient;

    @Override
    public List<String> saveImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            String imageUrl;
            try {
                imageUrl = uploadImage(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }


    private String uploadImage(MultipartFile file) throws IOException {
        try {
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new IOException("Invalid file name");
            }
            // 在 MinIO 中设置对象名（即文件名）
            String objectName = System.currentTimeMillis() + "_" + originalFilename;

            // 获取文件输入流
            InputStream inputStream = file.getInputStream();

            // 检查桶是否存在，不存在则创建
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(Const.IMAGE_BUCKET_NAME).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(Const.IMAGE_BUCKET_NAME).build());
            }

            // 上传文件到 MinIO 桶中
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(Const.IMAGE_BUCKET_NAME)
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(file.getContentType())
                    .build());

            // 生成图片的访问链接
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(Const.IMAGE_BUCKET_NAME)
                            .object(objectName)
                            .method(Method.GET)
                            .build());
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            // 处理异常
            e.printStackTrace();
            throw new IOException("Failed to upload image");
        }
    }


}
