package com.fordece.forum.entity.vo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PostVO implements Serializable {
    private Long id;
    private Long authorId;
    private String authorName;
    private Long commentCount;
    private Long upvoteCount;
    private String tags;
    private String title;
    private String content;
    private String contentJson;
    private Date latestRepliedTime;
    private Date createTime;
    private Date modifyTime;
}
