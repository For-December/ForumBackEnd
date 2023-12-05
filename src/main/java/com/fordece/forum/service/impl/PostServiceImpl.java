package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.entity.dto.Post;
import com.fordece.forum.entity.vo.request.CreatePostVO;
import com.fordece.forum.mapper.PostMapper;
import com.fordece.forum.service.AccountService;
import com.fordece.forum.service.MinioService;
import com.fordece.forum.service.PostService;
import com.fordece.forum.service.StarService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Resource
    AccountService accountService;

    @Override
    public Post getPostById(Long id) {
        return this.query().eq("id", id).one();
    }

    @Override
    @Cacheable(cacheNames = "posts",
            key = "'latest'",
            condition = "#pageNum>=0 && #pageSize>=0 &&" +
                    " #pageNum==0 && #pageSize==10")
    public List<Post> fetchPosts(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        log.info("posts 查库！！");
        // 创建条件构造器
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("latest_replied_time"); // 设置时间范围条件
        Page<Post> page = new Page<>(pageNum, pageSize);
        IPage<Post> postPage = this.baseMapper.selectPage(page, queryWrapper);// 不指定查询条件

        return postPage.getRecords();
    }

    @CacheEvict(cacheNames = "posts", key = "#key")
    public void clearCache(String key) {
        log.info("已清除缓存：" + key);
    }

    @Resource
    MinioService minioService;


    @Override
    public Boolean createPost(String content,
                              List<MultipartFile> images,
                              Long authorId,
                              String authorName,
                              String tags) {
        // 检验 id 合法性
        Account account = accountService.query().eq("id", authorId).one();
        if (!account.getUsername().equals(authorName)) {
            log.warn("存在用户顶替发贴：{} != {}", authorId, authorName);
            return false;
        }


        List<String> imageUrls = minioService.saveImages(images);
        log.warn(imageUrls.toString());

        Post post = new Post(null, authorId, authorName, 0L, 0L, tags, "标题", content, new Date(), new Date(), new Date(), null, false);

        return this.save(post);
    }


}
