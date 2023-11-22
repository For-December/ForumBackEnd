package com.fordece.forum.filter;

import com.fordece.forum.entity.RestBean;
import com.fordece.forum.utils.Const;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

// 限流过滤器
@Component
@Order(Const.ORDER_LIMIT)
public class FlowLimitFilter extends HttpFilter {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ip = request.getRemoteAddr();
        if (this.tryCount(ip)) {
            chain.doFilter(request, response);
            return;
        }
        this.writeBlockMessage(response);


    }

    private void writeBlockMessage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.forbidden("呃呃，压测是吧。别急~").asJsonString());
    }

    private boolean tryCount(String ip) {
        synchronized (ip.intern()) { // 并发有序
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(Const.FLOW_LIMIT_BLOCK + ip))) {
                return false; // 该 ip 已被封禁
            }
            return this.limitPeriodCheck(ip);
        }
    }

    private boolean limitPeriodCheck(String ip) {
        if (!Boolean.TRUE.equals(stringRedisTemplate.hasKey(Const.FLOW_LIMIT_COUNTER + ip))) {
            stringRedisTemplate.opsForValue().set(
                    Const.FLOW_LIMIT_COUNTER + ip, "1", 3, TimeUnit.SECONDS);
        }
        long increment = Optional.ofNullable(
                        stringRedisTemplate.opsForValue().increment(Const.FLOW_LIMIT_COUNTER + ip))
                .orElse(0L); // 到期则可能为空，这里让其为空时返回0
        if (increment > 25) { // 如果 3秒内请求了25次，则封禁 ip
            stringRedisTemplate.opsForValue().set(
                    Const.FLOW_LIMIT_BLOCK + ip, "", 25, TimeUnit.SECONDS);
            return false;
        }
        return true; // 否则正常放行


    }
}
