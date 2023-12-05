package com.fordece.forum.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fordece.forum.entity.RestBean;
import com.fordece.forum.entity.dto.Post;
import com.fordece.forum.entity.vo.request.CreatePostVO;
import com.fordece.forum.entity.vo.response.PostVO;
import com.fordece.forum.service.PostService;
import com.fordece.forum.service.StarService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        System.out.println(postPage.getRecords().get(0).toString());
        return ResponseEntity.ok(RestBean.success(postPage));


    }

    @PostMapping("")
    @PreAuthorize("#authorName == authentication.name") // 防止冒充发帖
    public ResponseEntity<RestBean<Void>> createPost(
            @Size(min = 1, max = 200, message = "帖子内容(content)的长度不合法") String content,
            List<MultipartFile> images,
            @NotNull(message = "帖子作者id不能为空")
            Long authorId,
            @NotNull(message = "帖子作者名字不能为空")
            String authorName,
            @NotNull(message = "标签必须指定")
            String tags

    ) { // 只有发帖名和token名一致才能发帖
        if (!postService.createPost(content, images, authorId, authorName, tags)) {
            return ResponseEntity.badRequest().body(RestBean.forbidden("请勿顶替别人发贴~"));
        }
        postService.clearCache("latest");
        return ResponseEntity.ok(RestBean.success());
    }


    @GetMapping("/{id}")
    public RestBean<PostVO> getPostById(@PathVariable Long id) { // 只有发帖名和token名一致才能发帖
        Post post = postService.getPostById(id);
        PostVO vo = post.asViewObject(PostVO.class);
        return RestBean.success(vo);
    }


    @Resource
    StarService starService;

    @PostMapping("/star/{postId}")
    @PreAuthorize("#userName == authentication.name")
    public RestBean<Boolean> setStarStatus(
            @PathVariable @Min((0)) Long postId,
            @RequestParam @Min(0) Long userId,
            @RequestParam @Min(0) String userName,
            @RequestParam @NotNull Boolean like) { // 点赞名和token一致
        log.info(userName);
        if (like) {
            starService.star(userId, postId);
        } else {
            starService.unStar(userId, postId);
        }
        return RestBean.success(like);
    }

    @GetMapping("/star/{postId}")
    @PreAuthorize("#userName == authentication.name")
    public RestBean<Boolean> getStarStatus(
            @PathVariable @Min((0)) Long postId,
            @RequestParam @Min(0) Long userId,
            @RequestParam @Min(0) String userName) { // 点赞名和token一致
        return RestBean.success(starService.status(userId, postId));
    }


    // 公共的
    @PostMapping("/stars")
    public RestBean<List<Long>> getStarsNumList(
            @RequestBody @NotNull @Length(min = 1) List<Long> postIdList) { // 点赞名和token一致

        System.out.println(postIdList);
        // 返回依此对应的点赞数，给前端合并
        return RestBean.success(starService.starNum(postIdList));
    }
}
