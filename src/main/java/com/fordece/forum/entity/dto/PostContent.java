package com.fordece.forum.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fordece.forum.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("db_post")
@AllArgsConstructor
public class PostContent implements BaseData, Serializable {
    @Serial
    private static final long serialVersionUID = 4139872165523319574L;

    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private Byte type;


}
