package com.fordece.forum.controller.exception;


import com.fordece.forum.entity.RestBean;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CommonController {
    @ExceptionHandler(Exception.class) // 处理参数错误的异常
    public ResponseEntity<RestBean<Void>> OtherException(Exception e) {
        log.warn("未处理的异常 [{}: {}]", e.getClass().getName(), e.getMessage());
        e.printStackTrace();
        return ResponseEntity.badRequest().body(RestBean.failure(400, e.getMessage()));
//        return ResponseEntity.badRequest().body(RestBean.failure(400, "请求参数有误，检查一下再试试~（详细日志在后端捏**）"));
    }

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class}) // 处理参数错误的异常
    public ResponseEntity<RestBean<Void>> validationException(Exception e) {
        if (e instanceof ValidationException) {
            log.warn("Resolve [{}: {}]", e.getClass().getName(), e.getMessage());
            return ResponseEntity.badRequest().body(RestBean.failure(400, e.getMessage()));
//            return ResponseEntity.badRequest().body(RestBean.failure(400,
//                    "请求参数有误，检查一下再试试~（详细日志在后端捏）"));
        }

        if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult
                        .getFieldErrors().stream()
                        .map(t -> t.getDefaultMessage() == null ?
                                t.getClass().getName() : t.getDefaultMessage())
                        .collect(Collectors.joining(", "));
                log.warn("Resolve [{}: {}]", "错误原因", errorMessage);
                return ResponseEntity.badRequest().body(RestBean.failure(400, errorMessage));

            }
//            return ResponseEntity.badRequest().body(RestBean.failure(400,
//                    "请求参数有误，检查一下再试试~（详细日志在后端捏）"));
        }
        return ResponseEntity.badRequest().body(RestBean.failure(500, "遇到了意料之外的情况，请联系管理员~"));

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestBean<Void>> accessDenied(AccessDeniedException e) {
        log.warn("权限不足:{}", e.getMessage());
        return ResponseEntity.badRequest().body(RestBean.failure(403, e.getMessage()));
//        return ResponseEntity.badRequest().body(RestBean.failure(403, "权限不足~（详细日志在后端捏）"));
    }

}

