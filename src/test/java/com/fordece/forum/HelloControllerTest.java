package com.fordece.forum;

import com.fordece.forum.utils.Const;
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
}