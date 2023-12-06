package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.dto.Star;
import com.fordece.forum.mapper.StarMapper;
import com.fordece.forum.service.RedisStarService;
import com.fordece.forum.service.StarService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StarServiceImpl extends ServiceImpl<StarMapper, Star> implements StarService {


    @Resource
    private RedisStarService redisStarService;

    @Override
    public void star(Long userId, Long postId) {
        if (redisStarService.starStatus(userId, postId)) {
            return;
        }
        // 仅当状态发生变化时才++点赞数
        redisStarService.saveStarToRedis(userId, postId);
        redisStarService.incrementStarCount(postId);
    }

    @Override
    public void unStar(Long userId, Long postId) {
        if (!redisStarService.starStatus(userId, postId)) {
            return;
        }
        redisStarService.saveUnStarToRedis(userId, postId);
        redisStarService.decrementStarCount(postId);


    }

    @Override
    public Boolean status(Long userId, Long postId) {
        return redisStarService.starStatus(userId, postId);
    }

    @Override
    public List<Long> starNum(List<Long> postIdList) {
        return postIdList.stream().map(
                t -> redisStarService.getStarNum(t)
        ).toList();
    }

    @Override
    public List<Long> staredPosts(Long userId) {
        return redisStarService.getStaredPostList(userId);
    }
}
