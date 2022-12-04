package com.example.demo.service;

import com.example.demo.dto.LoginDto;
import com.example.demo.entity.UmsMember;

import javax.servlet.http.HttpSession;

/**
 * 会员信息操作接口
 * Created by YuanJW on 2022/12/4.
 */
public interface UmsMemberService {

    /**
     * 获取手机验证码
     * @param phone
     * @param httpSession
     * @return
     */
    String getAuthCode(String phone, HttpSession httpSession);

    /**
     * 用户登录
     * @param loginDto
     * @param httpSession
     */
    void login(LoginDto loginDto, HttpSession httpSession);

    /**
     * 获取用户信息
     * @return
     */
    UmsMember info();
}
