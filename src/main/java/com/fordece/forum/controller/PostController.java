package com.fordece.forum.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fordece.forum.entity.RestBean;
import com.fordece.forum.entity.dto.Post;
import com.fordece.forum.entity.vo.request.CreatePostVO;
import com.fordece.forum.entity.vo.response.PostVO;
import com.fordece.forum.service.PostService;
import com.fordece.forum.service.StarService;
import com.fordece.forum.utils.ChatGPTUtils;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@Validated // 添加该注解，单参数校验才会生效！
public class PostController {
    @Resource
    PostService postService;

    @GetMapping("")
    public ResponseEntity<RestBean<IPage<PostVO>>> getPosts(@Min(0) Integer pageNum, @Min(1) Integer pageSize) {

        List<Post> posts = postService.fetchPosts(pageNum, pageSize);
        Page<PostVO> postPage = new Page<>();
        postPage.setRecords(
                posts.stream().map(
                        t -> t.asViewObject(PostVO.class)
                ).toList());
        System.out.println(postPage.getRecords().get(0).toString());
        return ResponseEntity.ok(RestBean.success(postPage));


    }

    @Resource
    ChatGPTUtils chatGPTUtils;

    @PostMapping("")
    @PreAuthorize("#authorName == authentication.name") // 防止冒充发帖
    public ResponseEntity<RestBean<Void>> createPost(
            @RequestParam
            @NotNull(message = "必须填写文本内容")
            @Length(min = 1, max = 200, message = "帖子内容(text)的长度不合法")
            String text, /* 帖子文本 */

//            @RequestParam
            // 图片可以为空
            List<MultipartFile> images, /* 贴子图片 */

            @RequestParam
            @NotNull(message = "帖子作者id不能为空")
            Long authorId, /* 发帖人Id */

            @RequestParam @NotNull(message = "帖子作者名字不能为空")
            String authorName, /* 发帖人名 */

            @RequestParam @NotNull(message = "标签必须指定")
            String tags /* 贴子标签 */

    ) {
        if (images == null) {
            images = new ArrayList<>();
        }
        // 只有发帖名和token名一致才能发帖
        String check = chatGPTUtils.check(text);
        if (check != null) {
            return ResponseEntity.badRequest().body(RestBean.forbidden("贴子审核未通过=>" + check));
        }
        if (!postService.createPost(text, images, authorId, authorName, tags)) {
            return ResponseEntity.badRequest().body(RestBean.forbidden("请勿顶替别人发贴~"));
        }
        postService.clearCache("latest");
        return ResponseEntity.ok(RestBean.success());
    }


    @GetMapping("/{id}")
    public RestBean<PostVO> getPostById(@PathVariable @NotNull Long id) { // 只有发帖名和token名一致才能发帖
        Post post = postService.getPostById(id);
        PostVO vo = post.asViewObject(PostVO.class);
        return RestBean.success(vo);
    }


    @Resource
    StarService starService;

    @PostMapping("/star/{postId}")
    @PreAuthorize("#userName == authentication.name")
    public RestBean<Boolean> setStarStatus(
            @PathVariable @Min(0) Long postId,
            @RequestParam @Min(0) Long userId,
            @RequestParam @Length String userName,
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
            @RequestParam @Length String userName) { // 点赞名和token一致
        return RestBean.success(starService.status(userId, postId));
    }


    // 公共的
    @PostMapping("/stars")
    public RestBean<List<Long>> getStarsNumList(
            @RequestBody @NotNull @Size(min = 1) List<Long> postIdList) { // 点赞名和token一致

        System.out.println(postIdList);
        // 返回依此对应的点赞数，给前端合并
        return RestBean.success(starService.starNum(postIdList));
    }
}
