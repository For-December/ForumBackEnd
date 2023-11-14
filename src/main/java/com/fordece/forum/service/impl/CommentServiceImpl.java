package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.dto.Comment;
import com.fordece.forum.entity.dto.Post;
import com.fordece.forum.entity.vo.request.CreateCommentVO;
import com.fordece.forum.mapper.CommentMapper;
import com.fordece.forum.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Override
    public Comment getCommentById(Long id) {
        return null;
    }

    @Override
    @Cacheable(cacheNames = "comments",
            key = "#postId",
            condition = "#pageNum>=0 && #pageSize>=0 &&" +
                    " #pageNum==0 && #pageSize==10")
    public List<Comment> fetchComments(Long postId, Integer pageNum, Integer pageSize) {
        log.info("comments 查库！！");
        // 创建条件构造器
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        queryWrapper.orderByDesc("latest_replied_time"); // 设置时间范围条件
        Page<Comment> page = new Page<>(pageNum, pageSize);
        IPage<Comment> commentPage = this.baseMapper.selectPage(page, queryWrapper);// 不指定查询条件
        return commentPage.getRecords();
    }

    @Override
    public Boolean createComment(CreateCommentVO vo) {
        return null;
    }

    @Override
    @CacheEvict(cacheNames = "comments", key = "#key")
    public void clearCache(String key) {
        log.info("已清除缓存：" + key);
    }
}
