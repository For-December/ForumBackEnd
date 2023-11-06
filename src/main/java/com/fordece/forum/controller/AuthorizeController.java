package com.fordece.forum.controller;

import com.fordece.forum.entity.RestBean;
import com.fordece.forum.entity.vo.request.EmailRegisterVO;
import com.fordece.forum.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;


@Validated
@RestController
@RequestMapping("/api/v1/auth")
public class AuthorizeController {

    @Resource
    AccountService accountService;


    @GetMapping("/ask-code")
    public ResponseEntity<RestBean<Void>>
    askVerifyCode(@RequestParam @Email @Length(min = 4, max = 25) String email,
                  @RequestParam @Pattern(regexp = "(register|reset)") String type,
                  HttpServletRequest request) {

        return this.messageHandle(() ->
                accountService.registerEmailVerifyCode(type, email, request.getRemoteAddr()));


    }

    @PostMapping("/register")
    public ResponseEntity<RestBean<Void>> register(@RequestBody EmailRegisterVO vo) {
        return this.messageHandle(() ->
                accountService.registerEmailAccount(vo));

    }


    private ResponseEntity<RestBean<Void>> messageHandle(Supplier<String> action) {
        String message = action.get();
        return message == null ?
                ResponseEntity.ok(RestBean.success()) :
                ResponseEntity.badRequest().body(RestBean.failure(400, message));
    }


}
