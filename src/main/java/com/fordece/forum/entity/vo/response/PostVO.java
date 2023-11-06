package com.fordece.forum.entity.vo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PostVO implements Serializable {
    private Long id;
    private Long authorId;
    private Long commentCount;
    private Long upvoteCount;
    private String tags;
    private String title;
    private String content;
    private Date latestRepliedTime;
    private Date createTime;
    private Date modifyTime;
}
