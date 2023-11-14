package com.fordece.forum.entity.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fordece.forum.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("db_account")
@AllArgsConstructor
public class Account implements BaseData, Serializable {

    @Serial
    private static final long serialVersionUID = -2093426521462799511L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String nickname;
    private String username;
    private String password;
    private Byte status; // 0 正常 1 停用
    private String email;
    private String avatar; // 头像路径（不要存二进制！
    private Long balance; // 用户声望什么的
    private String role;
    private Date registerTime;
    private Date modifyTime;
    private Date deleteTime;

    //逻辑删除（0 未删除、1 删除）
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Byte isDelete; // 是否已删除，删除则清空其他数据，已发布帖子变为匿名用户
}
