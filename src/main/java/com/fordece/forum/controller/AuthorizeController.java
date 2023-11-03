package com.fordece.forum.controller;

import com.fordece.forum.entity.RestBean;
import com.fordece.forum.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthorizeController {

    @Resource
    AccountService accountService;


    @GetMapping("/ask-code")
    public ResponseEntity<RestBean<String>> askVerifyCode(@RequestParam String email,
                                                          @RequestParam String type,
                                                          HttpServletRequest request) {

        String message = accountService.registerEmailVerifyCode(type, email, request.getRemoteAddr());
        return message == null ?
                ResponseEntity.ok(RestBean.success()) :
                ResponseEntity.badRequest().body(RestBean.failure(400, message));


    }


}
