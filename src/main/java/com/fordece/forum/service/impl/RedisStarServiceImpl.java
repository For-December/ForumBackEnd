package com.fordece.forum.service.impl;

import com.fordece.forum.entity.LikedStatusEnum;
import com.fordece.forum.entity.dto.Star;
import com.fordece.forum.service.RedisStarService;
import com.fordece.forum.utils.Const;
import com.fordece.forum.utils.RedisKeyUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class RedisStarServiceImpl implements RedisStarService {
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Override
    public void saveStarToRedis(Long userId, Long postId) {
        String key = RedisKeyUtils.getLikedKey(postId, userId);
        redisTemplate.opsForHash().put(Const.POST_USER_START_STATUS, key, LikedStatusEnum.LIKE.getCode());
    }

    @Override
    public void saveUnStarToRedis(Long userId, Long postId) {
        String key = RedisKeyUtils.getLikedKey(postId, userId);
        redisTemplate.opsForHash().put(Const.POST_USER_START_STATUS, key, LikedStatusEnum.UNLIKE.getCode());
    }

    @Override
    public Boolean starStatus(Long userId, Long postId) {
        String key = RedisKeyUtils.getLikedKey(postId, userId);
        Integer res = (Integer) redisTemplate.opsForHash().get(Const.POST_USER_START_STATUS, key);
        return res != null && res != 0;
    }


    @Override
    public void deleteStar(Long userId, Long postId) {

    }

    @Override
    public void incrementStarCount(Long postId) {
        // 原子操作~
        Long res = redisTemplate.opsForHash().increment(Const.POST_STAR_COUNTER, postId.toString(), 1);
        if (res != 1) throw new RuntimeException("增加后应当为1");

    }

    @Override
    public void decrementStarCount(Long postId) {
        Long res = redisTemplate.opsForHash().increment(Const.POST_STAR_COUNTER, postId.toString(), -1);
        if (res != 0) throw new RuntimeException("减少后应当为0");
    }

    @Override
    public List<Star> getStarDataList() {
        try (Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(Const.POST_STAR_COUNTER, ScanOptions.NONE)) {
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                String key = (String) entry.getKey();

                // 获取 postId 对应的评论数


//                //分离出 postId 和 userId
//                String[] split = key.split("::");
//                String postId = split[0];
//                String userId = split[1];
//                Integer value = (Integer) entry.getValue();
//
//
//                //存到 list 后从 Redis 中删除
//                redisTemplate.opsForHash().delete(Const.POST_STAR_COUNTER, key);
            }
        }

        return null;
    }

    @Override
    public Long getStarNum(Long postId) {
        Object res = redisTemplate.opsForHash().get(Const.POST_STAR_COUNTER, postId.toString());
        return res == null ? 0L : Long.parseLong(res.toString());
    }
}
