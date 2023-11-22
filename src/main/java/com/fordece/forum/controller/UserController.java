package com.fordece.forum.controller;

import com.fordece.forum.entity.RestBean;
import com.fordece.forum.entity.dto.Account;
import com.fordece.forum.entity.vo.response.AccountVO;
import com.fordece.forum.service.AccountService;
import com.fordece.forum.service.StarService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Resource
    AccountService accountService;

    @GetMapping("/info")
    @PreAuthorize("#username == authentication.name") // 必须写两个等号，是bool值！
    public ResponseEntity<RestBean<AccountVO>> info(String username) {
        Account account = accountService.findAccountByNameOrEmail(username);
        if (account == null) {
            // 不可能进到这里吧
            // 和用户名不匹配直接deny
            // 不对，管理员可以用这个
            return ResponseEntity.notFound().build();
        }
        AccountVO vo = account.asViewObject(AccountVO.class);

        return ResponseEntity.ok(RestBean.success(vo));
    }


    @Resource
    StarService starService;

    @GetMapping("/stars")
    public RestBean<List<Long>> getStaredPostList(
            @RequestParam @NotNull Long userId
    ) {
        return RestBean.success(starService.staredPosts(userId));
    }

//    @GetMapping("/:id")
//    public ResponseEntity<RestBean<AccountVO>> tets(){
//
//    }
}
