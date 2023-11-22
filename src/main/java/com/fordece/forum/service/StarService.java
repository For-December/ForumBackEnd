package com.fordece.forum.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fordece.forum.entity.dto.Star;

public interface StarService extends IService<Star> {

    void star(Long userId, Long postId);

    void unStar(Long userId, Long postId);

    Boolean status(Long userId, Long postId);

//    // 谁给该帖子点赞了？
//    Page<Star> getStarListByPostId(Long postId);
//
//    // 该用户给哪些贴子点赞了？
//    void saveStarCountToDB();
//
//    void saveStarToRedis();
//    void unStarFromRedis();
//    void deleteStarFromRedis();
//    void incrementStarCount();
//    void decrementStarCount();
//
//
//    void saveStarsToDB();
}
