package com.fordece.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.entity.vo.response.AccountVO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {


    Account findAccountByNameOrEmail(String text);

}
