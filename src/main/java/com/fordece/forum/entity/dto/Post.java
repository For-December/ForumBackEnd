package com.fordece.forum.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fordece.forum.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("db_post")
@AllArgsConstructor
public class Post implements BaseData, Serializable {

    @Serial
    private static final long serialVersionUID = 98799444712928355L;
    @TableId(type = IdType.AUTO)
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
    private Date DeleteTime;

}
