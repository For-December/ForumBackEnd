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
@TableName("db_comment")
@AllArgsConstructor
public class Comment implements BaseData, Serializable {
    @Serial
    private static final long serialVersionUID = -2494773832436312987L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long authorId;
    private String authorName;
    private Long upvoteCount;
    private String content;
    private Date createTime;
    private Date modifyTime;
    private Date DeleteTime;
}
