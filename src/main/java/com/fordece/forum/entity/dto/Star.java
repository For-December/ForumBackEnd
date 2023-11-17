package com.fordece.forum.entity.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_stars")
@AllArgsConstructor
public class Star {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long postId;
    private Long userId;
    private String userName;
    private Byte type; // 0 贴子 1 评论
    private Date createTime;
    private Date modifyTime;
    private Date deleteTime;

    //逻辑删除（0 未删除、1 删除）
    @TableLogic(value = "false", delval = "true")
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDelete;
}
