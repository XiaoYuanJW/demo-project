package com.example.demo.service;

import com.example.demo.dto.SignStatisticsDto;
import com.example.demo.entity.UmsMemberSign;

import java.util.List;

/**
 * 会员签到操作接口类
 * Created by YuanJW on 2023/1/5.
 */
public interface UmsMemberSignService {
    /**
     * 分页查询签到活动列表
     * @param umsMemberSign
     * @return
     */
    List<UmsMemberSign> page(UmsMemberSign umsMemberSign);

    /**
     * 查询签到活动详情
     * @param id
     * @return
     */
    UmsMemberSign detail(Long id);

    /**
     * 会员活动签到
     * @param signId
     */
    boolean sign(Long signId);

    /**
     * 查看签到统计
     * @param signId
     * @return
     */
    SignStatisticsDto statistics(Long signId);
}
