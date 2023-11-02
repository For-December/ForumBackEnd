package com.fordece.forum.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AccountVO {
    Long id;
    String nickname;
    String username;
    String email;
    String avatar; // 头像路径（不要存二进制！
    String balance; // 用户声望什么的
    String role;
    Date registerTime;
    Date modifyTime;
}
