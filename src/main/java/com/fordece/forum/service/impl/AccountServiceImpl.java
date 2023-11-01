package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.mapper.AccountMapper;
import com.fordece.forum.service.AccountService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByNameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User.withUsername(username) // 可能是邮箱
                .password(account.getPassword()).roles(account.getRole()).build();
    }


    @Override
    public Account findAccountByNameOrEmail(String text) {
        return this.query().eq("username", text).or().eq("email", text).one();
    }

}
