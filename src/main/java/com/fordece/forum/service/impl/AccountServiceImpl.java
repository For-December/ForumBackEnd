package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.mapper.AccountMapper;
import com.fordece.forum.service.AccountService;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByNameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        System.out.println("tet");
        val s = User.withUsername(username) // 可能是邮箱
                .password(account.getPassword())
                .authorities(new SimpleGrantedAuthority("tesadskngfklbdsgt"))
                .build(); // role和authorities只能生效一个
        System.out.println(s);
        return s;
    }


    @Override
    public Account findAccountByNameOrEmail(String text) {
        System.out.println("查库！！！！！！");
        return this.query().eq("username", text).or().eq("email", text).one();
    }

    private static List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("useradf"));
        System.out.println(new SimpleGrantedAuthority("user/test")+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa#############################################");
//        return authorities.toArray(new GrantedAuthority[authorities.size()]);
        return authorities;
    }

}
