package com.fordece.forum.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@TableName("db_stars")
@AllArgsConstructor
public class Star {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long postId;
    private Long starUserId;
    private String starUserName;
}
