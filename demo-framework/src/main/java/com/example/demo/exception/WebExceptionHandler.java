package com.example.demo.exception;

import cn.hutool.core.collection.CollectionUtil;
import com.example.demo.api.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 全局异常处理类
 * Created by YuanJW on 2022/12/2.
 */
@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public CommonResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        // 获取请求入口
        String requestURI = request.getRequestURI();
        // 日志管理
        log.error("请求地址：{}，异常：{}", requestURI, e.getMessage());
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult handleValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        // 获取请求入口
        String requestURI = request.getRequestURI();
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        // 日志管理
        log.error("请求地址：{}，异常：{}", requestURI, message);
        return CommonResult.validateFailed(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult handleVilationException(ConstraintViolationException e, HttpServletRequest request) {
        // 获取请求入口
        String requestURI = request.getRequestURI();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        String message = null;
        if (CollectionUtil.isNotEmpty(constraintViolations)) {
            StringBuilder messageBuilder = new StringBuilder();
            constraintViolations.forEach(new Consumer<ConstraintViolation<?>>() {
                @Override
                public void accept(ConstraintViolation<?> constraintViolation) {
                    messageBuilder.append(constraintViolation.getMessage()).append(",");
                }
            });
            message = messageBuilder.toString();
        }
        // 日志管理
        log.error("请求地址：{}，异常：{}", requestURI, message);
        return CommonResult.validateFailed(message);
    }

    @ExceptionHandler(value = BindException.class)
    public CommonResult handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }
}
