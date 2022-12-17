package com.example.demo.service;

import com.example.demo.entity.SmsCouponHistory;

/**
 * 优惠券记录操作接口类
 * Created by YuanJW on 2022/12/9.
 */
public interface SmsCouponHistoryService {

    /**
     * 生成优惠券历史记录
     * @param smsCouponHistory
     * @return
     */
    int insert(SmsCouponHistory smsCouponHistory);

    /**
     * 购买优惠券
     * @param id
     * @return
     */
    long purchase(Long id);

    /**
     * 异步购买优惠券
     * @param id
     * @return
     */
    long asyncPurchase(Long id);
}
