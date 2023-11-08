package com.fordece.forum.entity.vo.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostVO {

    @Size(min = 1, max = 200, message = "帖子内容(content)的长度不合法")
    private String content;

    @NotNull(message = "帖子作者id不能为空")
    private Long authorId;

    @NotNull(message = "帖子作者名字不能为空")
    private String authorName;

    @NotNull(message = "标签必须指定")
    private String tags;


}
