package com.fordece.forum.service.impl;

import com.fordece.forum.service.RedisCommentService;
import com.fordece.forum.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCommentServiceImpl implements RedisCommentService {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Override
    public Boolean incrementCommentCount(Long postId) {
        Long res = redisTemplate.opsForHash().increment(Const.POST_COMMENT_COUNTER, postId.toString(), 1);
        if (res == 0) throw new RuntimeException("评论数不可能为负！！");
        return true;
    }

    @Override
    public Boolean decrementCommentCount(Long postId) {
        Long res = redisTemplate.opsForHash().increment(Const.POST_COMMENT_COUNTER, postId.toString(), -1);
        if (res < 0) throw new RuntimeException("评论数不可能为负！");
        return true;
    }
}
