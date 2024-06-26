package com.example.demo.interceptor;

import com.example.demo.dto.MemberDto;
import com.example.demo.service.UmsMemberCacheService;
import com.example.demo.util.MemberHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 刷新token拦截器
 * Created by YuanJW on 2022/12/6.
 */
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
    private UmsMemberCacheService umsMemberCacheService;

    private String tokenHeader;

    private String tokenHead;

    public TokenInterceptor(UmsMemberCacheService umsMemberCacheService, String tokenHeader, String tokenHead) {
        this.umsMemberCacheService = umsMemberCacheService;
        this.tokenHeader = tokenHeader;
        this.tokenHead = tokenHead;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的信息
        String header = request.getHeader(this.tokenHeader);
        if (header != null && header.startsWith(this.tokenHead)) {
            // 获取token
            String token = header.substring(this.tokenHead.length());
            // 从redis获取用户信息
            MemberDto member = umsMemberCacheService.getMember(token);
            if (member != null) {
                // 保存用户信息到ThreadLocal
                MemberHolder.set(member);
                // 刷新token有效期
                umsMemberCacheService.fresh(token);
                return true;
            }
        }
        return true;
    }
}
