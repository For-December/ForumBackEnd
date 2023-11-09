package com.fordece.forum.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fordece.forum.entity.RestBean;
import com.fordece.forum.entity.dto.Post;
import com.fordece.forum.entity.vo.request.CreatePostVO;
import com.fordece.forum.entity.vo.response.PostVO;
import com.fordece.forum.service.PostService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    @Resource
    PostService postService;

    @GetMapping("")
    public ResponseEntity<RestBean<IPage<PostVO>>> getPosts(Integer pageNum, Integer pageSize) {

        List<Post> posts = postService.fetchPosts(pageNum, pageSize);
        Page<PostVO> postPage = new Page<>();
        postPage.setRecords(
                posts.stream().map(
                        t -> t.asViewObject(PostVO.class)
                ).toList());

        return ResponseEntity.ok(RestBean.success(postPage));


    }

    @PostMapping("")
    @PreAuthorize("#vo.authorName == authentication.name") // 防止冒充发帖
    public ResponseEntity<RestBean<Void>> createPost(@RequestBody @Valid CreatePostVO vo) { // 只有发帖名和token名一致才能发帖
        if (!postService.createPost(vo)) {
            return ResponseEntity.badRequest().body(RestBean.forbidden("请勿顶替别人发贴~"));
        }
        postService.clearCache("latest");
        return ResponseEntity.ok(RestBean.success());
    }
}
