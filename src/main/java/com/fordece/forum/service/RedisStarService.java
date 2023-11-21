package com.fordece.forum.service;

import com.fordece.forum.entity.dto.Star;

import java.util.List;

public interface RedisStarService {

    void saveStar(String userId, String postId);

    void unStar(String userId,String postId);

    void deleteStar(String userId,String postId);

    void incrementStarCount(String userId);

    void decrementStarCount(String userId);

    List<Star> getStarDataList();


}
