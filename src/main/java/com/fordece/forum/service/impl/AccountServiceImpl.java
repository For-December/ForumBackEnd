package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.mapper.AccountMapper;
import com.fordece.forum.service.AccountService;
import com.fordece.forum.utils.Const;
import com.fordece.forum.utils.FlowUtils;
import jakarta.annotation.Resource;
import lombok.val;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByNameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        val s = User.withUsername(username) // 可能是邮箱
                .password(account.getPassword())
                .authorities(new SimpleGrantedAuthority("user/test"))
                .build(); // role和authorities只能生效一个
        System.out.println(s);
        return s;
    }


    @Override
    public Account findAccountByNameOrEmail(String text) {
        System.out.println("查库！！！！！！");
        return this.query().eq("username", text).or().eq("email", text).one();
    }

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    FlowUtils flowUtils;

    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {

        // 加锁，防止同一个 ip 的疯狂请求（压测）
        synchronized (ip.intern()) {
            if (!this.verifyLimit(ip)) {
                return "请求频繁，稍后再试！";
            }
            Random random = new Random();
            Integer code = random.nextInt(900000) + 100000; // 大于等于0且小于bound
            Map<String, String> data = Map.of("type", type, "email", email, "code", code.toString());
            amqpTemplate.convertAndSend("mail", data);
            stringRedisTemplate.opsForValue().set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);

            return null;
        }
    }

    private boolean verifyLimit(String ip) {
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }

    private static List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("user/test"));
        return authorities;
    }

}
