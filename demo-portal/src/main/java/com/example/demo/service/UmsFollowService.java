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
     * @param followee
     * @return
     */
    int follow(Long followee);

    /**
     * 会员取关
     * @param followee
     * @return
     */
    int unfollow(Long followee);

    /**
     * 查询会员是否关注
     * @param followee
     * @return
     */
    boolean isFollow(Long followee);

    List<MemberDto> common(Long id);
}
