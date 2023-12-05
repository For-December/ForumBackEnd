package com.fordece.forum;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
public class MinioTests {
    @Autowired
    private MinioClient minioClient;
    @Test
    public void testMinioClientPositive() {
        Assertions.assertNotNull(minioClient);
    }

    @Test
    public void testBase(){
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
