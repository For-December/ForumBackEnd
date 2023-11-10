package com.fordece.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.entity.vo.request.EmailRegisterVO;
import com.fordece.forum.entity.vo.response.AccountVO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;

// 接口可以多继承
public interface AccountService extends IService<Account>, UserDetailsService {


    Account findAccountByNameOrEmail(String text);

    String registerEmailVerifyCode(String type, String email, String ip);
    String registerEmailAccount(EmailRegisterVO emailRegisterVO);
}
