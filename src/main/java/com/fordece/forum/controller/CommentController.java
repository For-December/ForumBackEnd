package com.fordece.forum.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fordece.forum.entity.RestBean;
import com.fordece.forum.entity.dto.Comment;
import com.fordece.forum.entity.vo.request.CreateCommentVO;
import com.fordece.forum.entity.vo.response.CommentVO;
import com.fordece.forum.service.CommentService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    @Resource
    CommentService commentService;

    @GetMapping("")
    public ResponseEntity<RestBean<IPage<CommentVO>>> getComments(Long postId, Integer pageNum, Integer pageSize) {

        List<Comment> comments = commentService.fetchComments(postId, pageNum, pageSize);
        Page<CommentVO> commentPage = new Page<>();
        commentPage.setRecords(
                comments.stream().map(
                        t -> t.asViewObject(CommentVO.class)
                ).toList());
//        System.out.println(commentPage.getRecords().get(0).toString());
        return ResponseEntity.ok(RestBean.success(commentPage));


    }

    @PostMapping("")
    @PreAuthorize("#vo.authorName == authentication.name") // 防止冒充发帖
    public ResponseEntity<RestBean<Void>> createComment(@RequestBody @Valid CreateCommentVO vo) { // 只有评论用户名和token名一致才能发帖
        if (!commentService.createComment(vo)) {
            return ResponseEntity.badRequest().body(RestBean.forbidden("请勿顶替别人评论~"));
        }
        commentService.clearCache(vo.getPostId().toString());
        return ResponseEntity.ok(RestBean.success());
    }


    @GetMapping("/{id}")
    public ResponseEntity<RestBean<CommentVO>> getCommentById(@PathVariable @Min(0) Long id) { // 必须大于等于0
        Comment comment = commentService.getCommentById(id);
        CommentVO vo = comment.asViewObject(CommentVO.class);
        return ResponseEntity.ok(RestBean.success(vo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#authorName == authentication.name") // 普通用户只能删除自己的评论
    public ResponseEntity<RestBean<Void>> deleteCommentById(
            @PathVariable @Min(0) Long id,
            @RequestParam @NotNull Long postId,
            @RequestParam @NotNull String authorName
    ) {
        if (commentService.query()
                .eq("id", id)
                .eq("post_id", postId)
                .eq("author_name", authorName)
                .oneOpt().isEmpty()) {// 评论不存在，或是用户尝试删除其他人的贴子
            log.warn("用户非法删除评论：id: {} authorName: {}", id, authorName);
            return ResponseEntity.badRequest().body(RestBean.forbidden("您只能删除自己已发布的评论~"));
        }


        if (commentService.removeById(id)) {
            commentService.clearCache(postId.toString());
            return ResponseEntity.ok(RestBean.success());
        }

        return ResponseEntity.badRequest().body(RestBean.forbidden("评论删除出错，请联系管理员~"));


    }
}
