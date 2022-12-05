package com.example.demo.interceptor;

import com.example.demo.api.ResultCode;
import com.example.demo.constant.UmsMemberConstant;
import com.example.demo.dto.MemberDto;
import com.example.demo.util.MemberHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 * Created by YuanJW on 2022/12/4.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取session
        HttpSession session = request.getSession();
        // 从session中获取用户信息
        MemberDto member = (MemberDto) session.getAttribute(UmsMemberConstant.Member.MEMBER_LOGIN);
        if (member == null) {
            // 拦截异常
            response.setStatus((int) ResultCode.UNAUTHORIZED.getCode());
            return false;
        }
        // 保存用户信息到ThreadLocal
        MemberHolder.set(member);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 从ThreadLocal中移除用户信息
        MemberHolder.remove();
    }
}
