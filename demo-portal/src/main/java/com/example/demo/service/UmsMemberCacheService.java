package com.example.demo.service;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.UmsMember;

/**
 * 用户信息缓存业务接口类
 * Created by YuanJW on 2022/12/5.
 */
public interface UmsMemberCacheService {
    /**
     * 设置验证码
     * @param phone
     * @param authCode
     */
    void setAuthCode(String phone, String authCode);

    /**
     * 获取验证码
     * @param phone
     * @return
     */
    String getAuthCode(String phone);

    /**
     * 设置用户信息
     * @param umsMember
     * @return token
     */
    String setMember(UmsMember umsMember);

    /**
     * 获取用户信息
     * @param token
     */
    MemberDto getMember(String token);

    /**
     * 刷新登录过期时间
     * @param token
     */
    void fresh(String token);
}
