package com.fordece.forum.entity.vo.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostVO {

    @Size(min = 1, max = 200, message = "帖子内容的长度不合法")
    private String content;

    private Long authorId;

    private String tags;


}
