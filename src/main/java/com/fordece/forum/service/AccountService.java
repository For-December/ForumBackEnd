package com.fordece.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.entity.vo.response.AccountVO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {

    // 请注意，缓存查库结果时，返回值需要实现 Serializable
    @Cacheable(cacheNames = "user", key = "#text", condition = "#text!=null")
    // 添加缓存
    Account findAccountByNameOrEmail(String text);

    String registerEmailVerifyCode(String type, String email, String ip);

}
