package com.fordece.forum.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fordece.forum.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_account")
@AllArgsConstructor
public class Account implements BaseData {
    @TableId(type = IdType.AUTO)
    Long id;
    String nickname;
    String username;
    String password;
    Byte status; // 0 正常 1 停用
    String email;
    String avatar; // 头像路径（不要存二进制！
    String balance; // 用户声望什么的
    String role;
    Date registerTime;
    Date modifyTime;
    Date deleteTime;
    Boolean isDelete; // 是否已删除，删除则清空其他数据，已发布帖子变为匿名用户
}
