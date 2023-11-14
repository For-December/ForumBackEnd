package com.fordece.forum.entity.vo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CommentVO implements Serializable {
    private Long id;
    private Long postId;
    private Long authorId;
    private String authorName;
    private Long upvoteCount;
    private String content;
    private Date createTime;
    private Date modifyTime;

}
