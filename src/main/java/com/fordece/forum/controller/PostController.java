package com.fordece.forum.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fordece.forum.entity.RestBean;
import com.fordece.forum.entity.dto.Post;
import com.fordece.forum.entity.vo.response.PostVO;
import com.fordece.forum.service.PostService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    @Resource
    PostService postService;

    @GetMapping("")
    public ResponseEntity<RestBean<IPage<PostVO>>> getPost(Integer pageNum, Integer pageSize) {

        List<Post> posts = postService.fetchPosts(pageNum, pageSize);
        Page<PostVO> postPage = new Page<>();
        postPage.setRecords(
                posts.stream().map(
                        t -> t.asViewObject(PostVO.class)
                ).toList());

        return ResponseEntity.ok(RestBean.success(postPage));


    }
}
