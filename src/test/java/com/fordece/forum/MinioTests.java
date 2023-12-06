package com.fordece.forum;

import com.fordece.forum.service.MinioService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class MinioTests {
    @Autowired
    private MinioClient minioClient;

    @Resource
    private MinioService minioService;

    @Test
    public void testService() throws IOException {
        ArrayList<MultipartFile> videos = new ArrayList<>();
        MultipartFile video = new MockMultipartFile("test", "testVideo.mp4", "video/mp4", new FileInputStream("C:\\Users\\forDece\\Desktop\\toJson\\视频.mp4"));
        videos.add(video);
        List<String> strings = minioService.saveVideos(videos);
        log.warn(strings.toString());

    }


    @Test
    public void testMinioClientPositive() {
        Assertions.assertNotNull(minioClient);
    }

    @Test
    public void testBase() {
        try {
            // 定义存储桶和对象名称
            String bucketName = "my-images";
            String objectName = "my-picture.jpg";
            String filePath = "C:/Users/forDece/Desktop/20200902140636_8C4PY.jpeg";

            // 上传对象到存储桶
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(filePath)
                            .build()
            );
            System.out.println("Uploaded object to bucket.");

            // 下载对象
            try (InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            )) {
                Files.copy(stream, Path.of("./downloaded-" + objectName), StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("Downloaded object from bucket.");

            // 删除对象
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            System.out.println("Deleted objecEt from bucket.");

        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
