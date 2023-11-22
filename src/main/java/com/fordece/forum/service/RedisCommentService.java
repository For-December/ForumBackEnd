package com.fordece.forum.service;

public interface RedisCommentService {

    Boolean incrementCommentCount(Long postId);
    Boolean decrementCommentCount(Long postId);

}
