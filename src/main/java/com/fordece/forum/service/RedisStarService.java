package com.fordece.forum.service;

import java.util.List;

public interface RedisStarService {

    void saveStarToRedis(Long userId, Long postId);

    void saveUnStarToRedis(Long userId, Long postId);

    Boolean starStatus(Long userId, Long postId);

    void deleteStar(Long userId, Long postId);

    void incrementStarCount(Long postId);

    void decrementStarCount(Long postId);

    List<Long> getStaredPostList(Long userId);

    Long getStarNum(Long postId);

}
