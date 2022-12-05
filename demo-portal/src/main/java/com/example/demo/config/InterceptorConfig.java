package com.example.demo.config;

import com.example.demo.interceptor.LoginInterceptor;
import com.example.demo.service.UmsMemberCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器配置
 * Created by YuanJW on 2022/12/4.
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private UmsMemberCacheService umsMemberCacheService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(umsMemberCacheService, tokenHeader, tokenHead))
                .excludePathPatterns(
                        "/member/getAuthCode",
                        "/member/login"
                );
    }
}