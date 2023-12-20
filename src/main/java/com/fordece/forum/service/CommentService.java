package com.fordece.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fordece.forum.entity.dto.Comment;
import com.fordece.forum.entity.vo.request.CreateCommentVO;

import java.util.List;

public interface CommentService extends IService<Comment> {
    /**
     *
     * @param id 贴子 id
     * @return com.fordece.forum.entity.dto.Comment
     * @author forDecember
     * @since 2023/12/20 16:45
     */
    Comment getCommentById(Long id);

    /**
     * 分页查询评论
     *
     * @param postId 贴子id
     * @param pageNum 分页号
     * @param pageSize 分页大小
     * @return java.util.List<com.fordece.forum.entity.dto.Comment>
     * @author forDecember
     * @since 2023/12/20 16:46
     */
    List<Comment> fetchComments(Long postId, Integer pageNum, Integer pageSize);

    /**
     * 创建频率
     *
     * @param vo 封装好的请求参数（之前的设计）
     * @return java.lang.Boolean
     * @author forDecember
     * @since 2023/12/20 16:48
     */
    Boolean createComment(CreateCommentVO vo);

    /**
     * 删除评论
     *
     * @param id 评论id
     * @param postId 对应的贴子 id
     * @param authorName 作者，用于二次校验
     * @return java.lang.Boolean
     * @author forDecember
     * @since 2023/12/20 16:49
     */
    Boolean deleteComment(Long id, Long postId, String authorName);

    /**
     * 清除评论缓存
     *
     * @param key 缓存的键
     * @return java.lang.Boolean
     * @author forDecember
     * @since 2023/12/20 16:49
     */
    Boolean clearCache(String key);

}
