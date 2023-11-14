package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.entity.vo.request.EmailRegisterVO;
import com.fordece.forum.mapper.AccountMapper;
import com.fordece.forum.service.AccountService;
import com.fordece.forum.utils.Const;
import com.fordece.forum.utils.FlowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 这里不会缓存，因为是内部类调用，
        // 不过同一个用户的登录一般也不会多次触发，无所谓了
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


    // 请注意，缓存查库结果时，返回值需要实现 Serializable
    // 添加缓存
    @Cacheable(cacheNames = "users", key = "#text", condition = "#text!=null")
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

    @Resource
    PasswordEncoder encoder;

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

    @Override
    public String registerEmailAccount(EmailRegisterVO emailRegisterVO) {
        String email = emailRegisterVO.getEmail();
        String key = Const.VERIFY_EMAIL_DATA + email;
        String code = stringRedisTemplate.opsForValue().get(key);
        if (code == null) return "请先获取验证码~";
        if (!code.equals(emailRegisterVO.getCode())) {
            log.warn("[验证码]=> redis: {} != user: {}", code, emailRegisterVO.getCode());
            return "验证码有误，请重新输入";
        }
        if (this.existsAccountByField("email", email)) {
            return "该email已被其他用户注册~";
        }
        if (this.existsAccountByField("username", emailRegisterVO.getUsername())) {
            return "该用户名已被注册~";
        }

        String password = encoder.encode(emailRegisterVO.getPassword());
        Account account = new Account(null, null, emailRegisterVO.getUsername(), password, (byte) 0, email, "avatar", 0L, "user", new Date(), null, null, (byte) 0);


        if (!this.save(account)) {
            return "注册用户时出现内部错误，请联系管理员";
        }
        stringRedisTemplate.delete(key); // 注册成功则删除验证码
        return null;
    }

    private boolean existsAccountByField(String field, String value) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq(field, value));
    }

    private boolean verifyLimit(String ip) {
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 30);
    }

    private static List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("user/test"));
        return authorities;
    }

}
