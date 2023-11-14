package com.fordece.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fordece.forum.entity.dto.Comment;
import com.fordece.forum.entity.vo.request.CreateCommentVO;

import java.util.List;

public interface CommentService extends IService<Comment> {
    Comment getCommentById(Long id);

    List<Comment> fetchComments(Long postId, Integer pageNum, Integer pageSize);

    Boolean createComment(CreateCommentVO vo);

    void clearCache(String key);

}
