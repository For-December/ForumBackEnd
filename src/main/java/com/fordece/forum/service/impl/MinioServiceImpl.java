package com.fordece.forum.service.impl;

import com.fordece.forum.config.MinioConfig;
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
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MinioServiceImpl implements MinioService {
    @Resource
    MinioClient minioClient;

    @Resource
    MinioConfig minioConfig;

    @Override
    public List<String> saveImages(List<MultipartFile> images) {
        return saveFiles(images, Const.IMAGE_BUCKET_NAME);
    }

    @Override
    public List<String> saveVideos(List<MultipartFile> videos) {
        return saveFiles(videos, Const.VIDEO_BUCKET_NAME);
    }

    private List<String> saveFiles(List<MultipartFile> files, String bucketName) {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileUrl;
            try {
                fileUrl = uploadFile(file, bucketName);
            } catch (IOException e) {
                throw new RuntimeException(bucketName + ": " + e);
            }
            fileUrls.add(fileUrl);
        }
        return fileUrls;
    }


    private String uploadFile(MultipartFile file, String bucketName) throws IOException {
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
            if (!minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build())) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build());
                // 设置 Policy 为 Public
                minioClient.setBucketPolicy(
                        SetBucketPolicyArgs.builder()
                                .bucket(bucketName)
                                .config(publicBucketPolicy(bucketName))
                                .build()
                );
            }


            // 上传文件到 MinIO 桶中
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(file.getContentType())
                    .build());


            // 生成图片的访问链接
            return minioConfig.getAccessUrl(bucketName, objectName);


        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            // 处理异常
            throw new IOException("Failed to upload file", e);
        }
    }


    public static String publicBucketPolicy(String bucketName) {
        return "{\n" +
                "    \"Version\": \"2012-10-17\",\n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Effect\": \"Allow\",\n" +
                "            \"Principal\": {\n" +
                "                \"AWS\": [\n" +
                "                    \"*\"\n" +
                "                ]\n" +
                "            },\n" +
                "            \"Action\": [\n" +
                "                \"s3:GetBucketLocation\",\n" +
                "                \"s3:ListBucket\",\n" +
                "                \"s3:ListBucketMultipartUploads\"\n" +
                "            ],\n" +
                "            \"Resource\": [\n" +
                "                \"arn:aws:s3:::" + bucketName + "\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"Effect\": \"Allow\",\n" +
                "            \"Principal\": {\n" +
                "                \"AWS\": [\n" +
                "                    \"*\"\n" +
                "                ]\n" +
                "            },\n" +
                "            \"Action\": [\n" +
                "                \"s3:AbortMultipartUpload\",\n" +
                "                \"s3:DeleteObject\",\n" +
                "                \"s3:GetObject\",\n" +
                "                \"s3:ListMultipartUploadParts\",\n" +
                "                \"s3:PutObject\"\n" +
                "            ],\n" +
                "            \"Resource\": [\n" +
                "                \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }


}
