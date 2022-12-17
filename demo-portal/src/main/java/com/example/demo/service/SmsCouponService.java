package com.example.demo.service;

import com.example.demo.entity.SmsCoupon;

/**
 * 优惠券操作接口类
 * Created by YuanJW on 2022/12/16.
 */
public interface SmsCouponService {
    /**
     * 新增优惠券信息
     * @param smsCoupon
     * @return
     */
    int insert(SmsCoupon smsCoupon);

    /**
     * 修改优惠卷信息
     * @param smsCoupon
     * @return
     */
    int update(SmsCoupon smsCoupon);
}
