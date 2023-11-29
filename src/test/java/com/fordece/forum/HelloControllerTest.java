package com.fordece.forum;

import com.fordece.forum.utils.Const;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHelloWorld() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/world"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("Hello World!"));
    }

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Test
    public void testRedis() {

        redisTemplate.opsForHash().put("aa:cc:", "test", 1);
        redisTemplate.opsForHash().put("aa:bb:", "hello", 1);
        redisTemplate.opsForHash().increment(Const.POST_STAR_COUNTER, "postId", 1);
//        Object integer1 = redisTemplate.opsForHash().get(Const.POST_STAR_COUNTER, "postId");
        Integer integer2 = (Integer) redisTemplate.opsForHash().get("aa:bb:", "hello");
//        System.out.println(integer1);
        redisTemplate.opsForHash().increment("aa:bb:", "hello", 1);
        System.out.println(integer2);

    }

    @Test
    public void testMinio(){
        try {
            // 创建MinioClient对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint("http://192.168.226.134:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

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
            System.out.println("Error occurred: " + e);
        }
    }
}