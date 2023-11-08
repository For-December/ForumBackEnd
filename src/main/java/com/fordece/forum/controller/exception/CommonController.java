package com.fordece.forum.controller.exception;


import com.fordece.forum.entity.RestBean;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.badRequest().body(RestBean.failure(400, "请求参数有误，检查一下再试试~（详细日志在后端捏**）"));
    }

    @ExceptionHandler(ValidationException.class) // 处理参数错误的异常
    public ResponseEntity<RestBean<Void>> validationException(ValidationException e) {
        log.warn("Resolve [{}: {}]", e.getClass().getName(), e.getMessage());
        return ResponseEntity.badRequest().body(RestBean.failure(400, "请求参数有误，检查一下再试试~（详细日志在后端捏）"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestBean<Void>> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult
                    .getFieldErrors().stream()
                    .map(t -> t.getDefaultMessage() == null ?
                            t.getClass().getName() : t.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            log.warn("Resolve [{}: {}]", "错误原因", errorMessage);

        }
        return ResponseEntity.badRequest().body(RestBean.failure(400, "请求参数有误，检查一下再试试~（详细日志在后端捏）"));
    }

}

