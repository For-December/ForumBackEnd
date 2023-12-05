package com.fordece.forum;

import io.minio.MinioClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class MinioConfigTest {
    @Autowired
    private MinioClient minioClient;
    @Test
    public void testMinioClientPositive() {
        Assertions.assertNotNull(minioClient);
    }
}