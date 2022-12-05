package com.example.demo.service;

import com.example.demo.dto.LoginParam;
import com.example.demo.dto.MemberDto;

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
     * @param loginParam
     * @param httpSession
     */
    void login(LoginParam loginParam, HttpSession httpSession);

    /**
     * 获取用户信息
     * @return
     */
    MemberDto info();
}