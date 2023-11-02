package com.fordece.forum.controller;

import com.fordece.forum.entity.RestBean;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.entity.vo.response.AccountVO;
import com.fordece.forum.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Resource
    AccountService accountService;
    @GetMapping("/info")
    @PreAuthorize("#username == authentication.name") // 必须写两个等号，是bool值！
    public ResponseEntity<RestBean<AccountVO>> info(String username){
        Account account = accountService.findAccountByNameOrEmail(username);
        if (account==null){
            return ResponseEntity.notFound().build();
        }
        AccountVO vo = account.asViewObject(AccountVO.class);

        return ResponseEntity.ok(RestBean.success(vo));
    }
}
