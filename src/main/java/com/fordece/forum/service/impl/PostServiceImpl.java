package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.dto.Post;
import com.fordece.forum.mapper.PostMapper;
import com.fordece.forum.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Override
    public Post getPostById(Long id) {
        return null;
    }

    @Override
    public List<Post> fetchPosts(Integer pageNum, Integer pageSize) {
        Page<Post> page = new Page<>(pageNum,pageSize);
        IPage<Post> postPage = this.baseMapper.selectPage(page, null);// 不指定查询条件

        return postPage.getRecords();
    }
}
