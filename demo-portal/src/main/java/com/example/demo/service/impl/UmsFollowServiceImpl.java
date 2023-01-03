package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.dto.MemberDto;
import com.example.demo.entity.UmsFollow;
import com.example.demo.entity.UmsMember;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UmsFollowMapper;
import com.example.demo.mapper.UmsMemberMapper;
import com.example.demo.service.RedisService;
import com.example.demo.service.UmsFollowService;
import com.example.demo.service.UmsMemberService;
import com.example.demo.util.MemberHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 会员关注操作接口实现类
 * Created by YuanJW on 2023/1/2.
 */
@Slf4j
@Service
public class UmsFollowServiceImpl implements UmsFollowService {
    @Resource
    private UmsFollowMapper umsFollowMapper;
    @Resource
    private UmsMemberMapper umsMemberMapper;
    @Resource
    private UmsMemberService umsMemberService;
    @Resource
    private RedisService redisService;
    @Value("${redis.key.follow}")
    private String follewKey;

    @Override
    public int follow(Long followee) {
        // 校验关注用户是否存在
        UmsMember umsMember = umsMemberMapper.selectById(followee);
        if (umsMember == null) {
            throw new ServiceException("被关注会员信息不存在");
        }
        Long follower = MemberHolder.get().getId();
        // 避免自己关注自己
        if (followee.equals(follower)) {
            throw new ServiceException("不能自己关注自己");
        }
        // 校验用户是否已经关注指定用户
        UmsFollow umsFollow = umsFollowMapper.selectOne(new LambdaQueryWrapper<UmsFollow>()
                        .eq(UmsFollow::getFolloweeId, followee)
                        .eq(UmsFollow::getFollowerId, follower));
        if (umsFollow != null) {
            throw new ServiceException("您已经关注该用户");
        }
        int count = umsFollowMapper.insert(new UmsFollow()
                        .setFollowerId(follower)
                        .setFolloweeId(followee));
        if (count > 0) {
            // 将关注信息添加到redis的set集合中
            redisService.sAdd(follewKey + ":" + follower, followee);
        }
        return count;
    }

    @Override
    public int unfollow(Long followee) {
        Long follower = MemberHolder.get().getId();
        UmsFollow umsFollow = umsFollowMapper.selectOne(new LambdaQueryWrapper<UmsFollow>()
                        .eq(UmsFollow::getFolloweeId, followee)
                        .eq(UmsFollow::getFollowerId, follower));
        if (umsFollow == null) {
            throw new ServiceException("您未关注该用户");
        }
        int count = umsFollowMapper.deleteById(umsFollow.getId());
        if (count > 0) {
            // 将关注信息从redis的set集合中移除
            redisService.sRemove(follewKey + ":" + follower, followee);
        }
        return count;
    }

    @Override
    public boolean isFollow(Long followee) {
        Long follower = MemberHolder.get().getId();
        // 校验用户是否已经关注指定用户
        return BooleanUtil.isTrue(redisService.sIsMember(follewKey + ":" + follower, followee));
    }

    @Override
    public List<MemberDto> common(Long id) {
        List<String> keys = new ArrayList<>();
        Long followee = MemberHolder.get().getId();
        keys.add(follewKey + ":" + followee);
        keys.add(follewKey + ":" + id);
        Set<Object> commonFollowees = redisService.sInter(keys);
        if (commonFollowees == null || commonFollowees.isEmpty()) {
            // 交集为空的情况
            return Collections.emptyList();
        }
        List<Long> ids = commonFollowees.stream()
                .map(m -> Long.valueOf(m.toString()))
                .collect(Collectors.toList());
        return umsMemberService.listByIds(ids)
                .stream()
                .map(member -> BeanUtil.copyProperties(member, MemberDto.class))
                .collect(Collectors.toList());
    }
}
