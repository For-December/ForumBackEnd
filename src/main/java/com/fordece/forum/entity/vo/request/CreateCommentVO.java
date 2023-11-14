package com.fordece.forum.entity.vo.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCommentVO {

    @NotNull(message = "评论作者id不能为空")
    private Long authorId;

    @NotNull(message = "评论作者名字不能为空")
    private String authorName;

    @NotNull(message = "必须指定要评论的贴子")
    private Long postId;

    @Size(min = 1, max = 200, message = "评论内容(content)的长度不合法")
    private String content;


}
