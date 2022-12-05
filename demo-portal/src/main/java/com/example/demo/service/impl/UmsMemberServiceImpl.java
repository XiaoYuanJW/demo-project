package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.constant.UmsMemberConstant;
import com.example.demo.dto.LoginParam;
import com.example.demo.dto.MemberDto;
import com.example.demo.entity.UmsMember;
import com.example.demo.mapper.UmsMemberMapper;
import com.example.demo.service.UmsMemberService;
import com.example.demo.util.MemberHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 会员信息操作接口实现类
 * Created by YuanJW on 2022/12/4.
 */
@Slf4j
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    @Resource
    private UmsMemberMapper umsMemberMapper;

    @Override
    public String getAuthCode(String phone, HttpSession httpSession) {
        // 校验手机号
        if (!PhoneUtil.isPhone(phone)) {
            throw new RuntimeException("手机号错误，请重新输入！");
        }
        // 生成验证码
        String authCode = RandomUtil.randomNumbers(6);
        // 保存验证码至session
        httpSession.setAttribute(UmsMemberConstant.Member.AUTH_CODE + phone, authCode);
        // TODO : 发送验证码
        log.info("发送短信验证码至{}成功，验证码：{}", phone, authCode);
        return authCode;
    }

    @Override
    public void login(LoginParam loginParam, HttpSession httpSession) {
        // 校验手机号
        if (!PhoneUtil.isPhone(loginParam.getPhone())) {
            throw new RuntimeException("手机号错误，请重新输入！");
        }
        // 从session中获取验证码
        String authCode = (String) httpSession.getAttribute(UmsMemberConstant.Member.AUTH_CODE + loginParam.getPhone());
        // 校验验证码
        if (authCode == null || !authCode.equals(loginParam.getAuthCode())) {
            throw new RuntimeException("验证码错误！");
        }
        // 查询用户信息
        UmsMember umsMember = umsMemberMapper.selectOne(new LambdaQueryWrapper<UmsMember>()
                .eq(UmsMember::getPhone, loginParam.getPhone()));
        if (ObjectUtil.isNull(umsMember)) {
            // 创建新用户
            umsMember = UmsMember.builder()
                    .name(UmsMemberConstant.Member.NAME_PREFIX + RandomUtil.randomString(6))
                    .phone(loginParam.getPhone())
                    .password(RandomUtil.randomString(10))
                    .build();
            umsMemberMapper.insert(umsMember);
        }
        // 保存登录信息
        httpSession.setAttribute(UmsMemberConstant.Member.MEMBER_LOGIN, BeanUtil.copyProperties(umsMember, MemberDto.class));
    }

    @Override
    public MemberDto info() {
        return MemberHolder.get();
    }


}
