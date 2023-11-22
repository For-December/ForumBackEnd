package com.fordece.forum.utils;

public class RedisKeyUtils {


    public static String getLikedKey(Long postId, Long userId) {
        return postId + "::" + userId;
    }
}