package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.RestBean;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.entity.dto.Comment;
import com.fordece.forum.entity.dto.Post;
import com.fordece.forum.entity.vo.request.CreateCommentVO;
import com.fordece.forum.mapper.CommentMapper;
import com.fordece.forum.service.AccountService;
import com.fordece.forum.service.CommentService;
import com.fordece.forum.service.RedisCommentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    AccountService accountService;

    @Resource
    RedisCommentService redisCommentService;

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
        queryWrapper.orderByDesc("modify_time"); // 设置时间范围条件
        Page<Comment> page = new Page<>(pageNum, pageSize);
        IPage<Comment> commentPage = this.baseMapper.selectPage(page, queryWrapper);// 不指定查询条件
        return commentPage.getRecords();
    }

    @Override
    public Boolean createComment(CreateCommentVO vo) {
        // 检验 id 合法性
        Account account = accountService.query().eq("id", vo.getAuthorId()).one();
        if (!account.getUsername().equals(vo.getAuthorName())) {
            log.warn("存在用户顶替评论：{} != {}", vo.getAuthorId(), vo.getAuthorName());
            return false;
        }
        Comment comment = new Comment(null, vo.getPostId(), vo.getAuthorId(), vo.getAuthorName(), 0L, vo.getContent(), new Date(), new Date(), null, false);

        return this.save(comment)
                && redisCommentService
                .incrementCommentCount(vo.getPostId());
    }


    @Override
    public Boolean deleteComment(Long id, Long postId, String authorName) {
        if (this.query()
                .eq("id", id)
                .eq("post_id", postId)
                .eq("author_name", authorName)
                .oneOpt().isEmpty()) {// 评论不存在，或是用户尝试删除其他人的贴子
            log.warn("用户非法删除评论：id: {} authorName: {}", id, authorName);
            throw new RuntimeException("您只能删除自己已发布的评论~");
        }

        return removeById(id)
                && redisCommentService.decrementCommentCount(postId)
                && clearCache(postId.toString());

    }


    @Override
    @CacheEvict(cacheNames = "comments", key = "#key")
    public Boolean clearCache(String key) {
        log.info("已清除缓存：" + key);
        return true;
    }
}
