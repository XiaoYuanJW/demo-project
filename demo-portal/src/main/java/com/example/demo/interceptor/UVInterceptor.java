package com.example.demo.interceptor;

import com.example.demo.util.MemberHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * UV统计拦截器
 * Created by YuanJW on 2023/1/7.
 */
@Slf4j
public class UVInterceptor implements HandlerInterceptor {
    @Value("${redis.key.uv}")
    private String UVKey;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Long id = MemberHolder.get().getId();
        redisTemplate.opsForHyperLogLog().add(UVKey + date, id);
        return true;
    }
}
