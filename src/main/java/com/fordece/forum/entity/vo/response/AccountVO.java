package com.fordece.forum.entity.vo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccountVO implements Serializable {
    Long id;
    String nickname;
    String username;
//    String email; // 邮箱不对前端用户展示，保护隐私捏
    String avatar; // 头像路径（不要存二进制！
    Long balance; // 用户声望什么的
    String role;
    Date registerTime;
    Date modifyTime;
}
