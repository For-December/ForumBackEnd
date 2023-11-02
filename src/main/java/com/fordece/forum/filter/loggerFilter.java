package com.fordece.forum.filter;


import com.fordece.forum.utils.Const;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Order(-10)
public class loggerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 包装HttpServletRequest，把输入流缓存下来
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        // 包装HttpServletResponse，把输出流缓存下来
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(wrappedRequest, wrappedResponse);
        System.out.println(("http request:{}" + new String(wrappedRequest.getContentAsByteArray())));
        System.out.println("http response:{}" + new String(wrappedResponse.getContentAsByteArray()));
//        System.out.println(wrappedRequest.getHeader("test"));
        System.out.println(wrappedRequest.getHeader("Authorization"));
        wrappedResponse.getHeaderNames().forEach((name) -> {
            System.out.println(wrappedResponse.getHeader(name));
        });
        // 注意这一行代码一定要调用，不然无法返回响应体
        wrappedResponse.copyBodyToResponse();

    }
}
