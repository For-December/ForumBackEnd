package com.fordece.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fordece.forum.entity.dto.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {
    public Account findAccountByNameOrEmail(String text);
}
