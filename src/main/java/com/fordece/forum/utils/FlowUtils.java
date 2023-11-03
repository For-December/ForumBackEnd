package com.fordece.forum.utils;


import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

// 限流工具
@Component
public class FlowUtils {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    public boolean limitOnceCheck(String key, int blockTime) {
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            return false; // 不能直接请求，还在冷却时间中
        }
        stringRedisTemplate.opsForValue().set(key, "", blockTime, TimeUnit.SECONDS);
        return true;

    }
}
