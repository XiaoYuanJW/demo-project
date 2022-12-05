package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.annotation.CacheException;
import com.example.demo.dto.MemberDto;
import com.example.demo.entity.UmsMember;
import com.example.demo.mapper.UmsMemberMapper;
import com.example.demo.service.RedisService;
import com.example.demo.service.UmsMemberCacheService;
import com.example.demo.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户信息缓存业务实现类
 * Created by YuanJW on 2022/12/5.
 */
@Service
public class UmsMemberCacheServiceImpl implements UmsMemberCacheService {
    @Resource
    private RedisService redisService;
    @Resource
    private UmsMemberMapper umsMemberMapper;
    @Resource
    private JwtTokenUtils jwtTokenUtils;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.key.authCode}")
    private String REDIS_KEY_AUTH_CODE;
    @Value("${redis.key.member}")
    private String REDIS_KEY_MEMBER;
    @Value("${redis.expire.authCode}")
    private Long REDIS_EXPIRE_AUTH_CODE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE_COMMON;

    @CacheException
    @Override
    public void setAuthCode(String phone, String authCode) {
        String key =REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + phone;
        redisService.set(key, authCode, REDIS_EXPIRE_AUTH_CODE);
    }

    @CacheException
    @Override
    public String getAuthCode(String phone) {
        String key =REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + phone;
        return (String) redisService.get(key);
    }

    @Override
    public String setMember(UmsMember umsMember) {
        // 生成token作为登录令牌
        String token = jwtTokenUtils.generateToken(umsMember.getName());
        // 数据对象转换
        BeanUtil.copyProperties(umsMember, MemberDto.class);
        // 将对象转换为Hash结构
        Map<String, Object> value = BeanUtil.beanToMap(umsMember);
        // 保存用户信息至redis中
        redisService.hSetAll(REDIS_KEY_MEMBER + token, value, REDIS_EXPIRE_COMMON);
        return token;
    }

    @Override
    public MemberDto getMember(String token) {
        Map<Object, Object> map = redisService.hGetAll(REDIS_KEY_MEMBER + token);
        MemberDto memberDto = BeanUtil.fillBeanWithMap(map, new MemberDto(), false);
        return memberDto;
    }

    @Override
    public void fresh(String token) {
        redisService.expire(token, REDIS_EXPIRE_COMMON);
    }
}
