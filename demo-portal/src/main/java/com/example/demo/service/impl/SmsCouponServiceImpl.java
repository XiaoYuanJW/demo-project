package com.example.demo.service.impl;

import com.example.demo.entity.SmsCoupon;
import com.example.demo.mapper.SmsCouponMapper;
import com.example.demo.service.RedisService;
import com.example.demo.service.SmsCouponService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 优惠券操作实现类
 * Created by YuanJW on 2022/12/16.
 */
@Service
public class SmsCouponServiceImpl implements SmsCouponService {
    @Resource
    private SmsCouponMapper smsCouponMapper;
    @Resource
    private RedisService redisService;
    @Value("${redis.lock.couponStock}")
    private String COUPON_STOCK;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SmsCoupon smsCoupon) {
        if (smsCoupon.getSurplusCount() == null) {
            // 初始化库存信息
            smsCoupon.setSurplusCount(smsCoupon.getCount());
        }
        // 新增优惠券信息
        int count = smsCouponMapper.insert(smsCoupon);
        // 存储库存信息至redis
        redisService.set(COUPON_STOCK + ":" + smsCoupon.getId(), smsCoupon.getSurplusCount());
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmsCoupon smsCoupon) {
        // 修改优惠券信息
        int count = smsCouponMapper.updateById(smsCoupon);
        if (smsCoupon.getSurplusCount() != null) {
            // 存储库存信息至redis
            redisService.set(COUPON_STOCK + ":" + smsCoupon.getId(), smsCoupon.getSurplusCount());
        }
        return count;
    }
}
