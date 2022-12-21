package com.example.demo.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.constants.SysFileConstant;
import com.example.demo.constants.UmsMemberConstant;
import com.example.demo.dto.LoginParam;
import com.example.demo.dto.MemberDto;
import com.example.demo.entity.SysFile;
import com.example.demo.entity.UmsMember;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UmsMemberMapper;
import com.example.demo.service.SysFileService;
import com.example.demo.service.UmsMemberCacheService;
import com.example.demo.service.UmsMemberService;
import com.example.demo.util.MemberHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员信息操作接口实现类
 * Created by YuanJW on 2022/12/4.
 */
@Slf4j
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    @Resource
    private UmsMemberMapper umsMemberMapper;
    @Resource
    private UmsMemberCacheService umsMemberCacheService;
    @Resource
    private SysFileService sysFileService;

    @Override
    public String getAuthCode(String phone, HttpSession httpSession) {
        // 校验手机号
        if (!PhoneUtil.isPhone(phone)) {
            throw new ServiceException("手机号错误，请重新输入！");
        }
        // 生成验证码
        String authCode = RandomUtil.randomNumbers(6);
        // 保存验证码至session
//        httpSession.setAttribute(UmsMemberConstant.Member.AUTH_CODE + phone, authCode);
        // 保存验证码至redis
        umsMemberCacheService.setAuthCode(phone, authCode);
        // TODO : 发送验证码
        log.info("发送短信验证码至{}成功，验证码：{}", phone, authCode);
        return authCode;
    }

    @Override
    public String login(LoginParam loginParam, HttpSession httpSession) {
        // 校验手机号
//        if (!PhoneUtil.isPhone(loginParam.getPhone())) {
//            throw new ServiceException("手机号错误，请重新输入！");
//        }
        // 从session中获取验证码
//        String authCode = (String) httpSession.getAttribute(UmsMemberConstant.Member.AUTH_CODE + loginParam.getPhone());
        // 从redis中获取验证码
        String authCode = umsMemberCacheService.getAuthCode(loginParam.getPhone());
        // 校验验证码
        if (authCode == null || !authCode.equals(loginParam.getAuthCode())) {
            throw new ServiceException("验证码错误！");
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
        List<SysFile> fileList = sysFileService.getFileList(umsMember.getAvatar());
        Map<String, Object> avatar = MapUtil.builder(new HashMap<String, Object>()).put(SysFileConstant.Member.AVATAR, fileList).build();
        umsMember.setFiles(avatar);
        // 保存登录信息至session
//        httpSession.setAttribute(UmsMemberConstant.Member.MEMBER_LOGIN, BeanUtil.copyProperties(umsMember, MemberDto.class));
        // 保存登录信息至redis
        return umsMemberCacheService.setMember(umsMember);
    }

    @Override
    public MemberDto info() {
        return MemberHolder.get();
    }

    @Override
    public UmsMember detail(Long id) {
        UmsMember umsMember = umsMemberMapper.getUmsMemberById(id);
        Assert.notNull(umsMember, "用户信息异常");
        List<SysFile> fileList = sysFileService.getFileList(umsMember.getAvatar());
        umsMember.setAvatarInfo(fileList);
        return umsMember;
    }
}
