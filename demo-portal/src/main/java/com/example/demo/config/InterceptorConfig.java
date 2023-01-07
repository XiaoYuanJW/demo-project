package com.example.demo.config;

import com.example.demo.interceptor.LoginInterceptor;
import com.example.demo.interceptor.TokenInterceptor;
import com.example.demo.interceptor.UVInterceptor;
import com.example.demo.service.UmsMemberCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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
        // token拦截器
        registry.addInterceptor(new TokenInterceptor(umsMemberCacheService, tokenHeader, tokenHead))
                .addPathPatterns("/**")
                .order(0);
        // 登录验证拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/member/getAuthCode",
                        "/member/login"
                ).order(1);
        // UV统计拦截器
        registry.addInterceptor(uvInterceptor())
                .excludePathPatterns(
                        "/member/getAuthCode",
                        "/member/login"
                ).order(2);
    }

    @Bean
    public UVInterceptor uvInterceptor() {
        return new UVInterceptor();
    }
}