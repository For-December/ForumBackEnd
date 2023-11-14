package com.fordece.forum.entity.dto;

import com.baomidou.mybatisplus.annotation.*;
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
    private String authorName;
    private Long commentCount;
    private Long upvoteCount;
    private String tags;
    private String title;
    private String content;
    private Date latestRepliedTime;
    private Date createTime;
    private Date modifyTime;
    private Date deleteTime;

    //逻辑删除（0 未删除、1 删除）
    @TableLogic(value = "false", delval = "true")
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDelete; // 是否已删除，删除则清空其他数据，已发布帖子变为匿名用户

}
