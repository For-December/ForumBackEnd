package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.entity.dto.Post;
import com.fordece.forum.entity.vo.request.CreatePostVO;
import com.fordece.forum.mapper.PostMapper;
import com.fordece.forum.service.AccountService;
import com.fordece.forum.service.PostService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Resource
    AccountService accountService;

    @Override
    public Post getPostById(Long id) {
        return null;
    }

    @Override
    public List<Post> fetchPosts(Integer pageNum, Integer pageSize) {
        Page<Post> page = new Page<>(pageNum, pageSize);
        IPage<Post> postPage = this.baseMapper.selectPage(page, null);// 不指定查询条件

        return postPage.getRecords();
    }

    @Override
    public Boolean createPost(CreatePostVO vo) {
        // 检验 id 合法性
        Account account = accountService.query().eq("id", vo.getAuthorId()).one();
        if (!account.getUsername().equals(vo.getAuthorName())) {
            log.warn("存在用户顶替发贴：{} != {}", vo.getAuthorId(), vo.getAuthorName());
            return false;
        }

        Post post = new Post(null, vo.getAuthorId(), vo.getAuthorName(), 0L, 0L, vo.getTags(), "标题", vo.getContent(), new Date(), new Date(), new Date(), null);
        this.save(post);
        return true;
    }
}
