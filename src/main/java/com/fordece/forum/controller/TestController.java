package com.fordece.forum.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('user/test')")// or hasRole('user') // 好好好，这里必须加 ROLE_前缀
    public String test() {
        return "Hello World!";

    }

    @GetMapping("/world")
//    @PreAuthorize("hasAuthority('admin')")
    public String world() {
        return "Hello World!";
    }

}
