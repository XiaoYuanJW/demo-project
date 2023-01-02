package com.example.demo.service;

import com.example.demo.dto.MemberDto;

import java.util.List;

/**
 * 会员关注操作接口类
 * Created by YuanJW on 2023/1/2.
 */
public interface UmsFollowService {
    /**
     * 会员关注
     * @param follower
     * @return
     */
    int follow(Long follower);

    /**
     * 会员取关
     * @param follower
     * @return
     */
    int unfollow(Long follower);

    /**
     * 查询会员是否关注
     * @param follower
     * @return
     */
    boolean isFollow(Long follower);

    List<MemberDto> common(Long id);
}
