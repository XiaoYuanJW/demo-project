package com.example.demo.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.SmsCoupon;
import com.example.demo.entity.SmsCouponHistory;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.SmsCouponHistoryMapper;
import com.example.demo.mapper.SmsCouponMapper;
import com.example.demo.service.RedisLockService;
import com.example.demo.service.SmsCouponHistoryService;
import com.example.demo.util.MemberHolder;
import com.example.demo.utils.IdGeneratorUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * 优惠券记录操作接口类
 * Created by YuanJW on 2022/12/9.
 */
@Service
public class SmsCouponHistoryServiceImpl implements SmsCouponHistoryService {
    @Resource
    private SmsCouponHistoryMapper smsCouponHistoryMapper;
    @Resource
    private SmsCouponMapper smsCouponMapper;
    @Resource
    private IdGeneratorUtils idGeneratorUtils;
    @Resource
    private RedisLockService redisLockService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Value("${redis.key.couponHistory}")
    private String REDIS_KEY_COUPON_HISTORY;
    @Value("${redis.lock.couponHistory}")
    private String REDIS_LOCK_COUPON_HISTORY;
    @Value("${redis.expire.lock}")
    private Long REDIS_EXPIRE_LOCK;

    @Override
    public int insert(SmsCouponHistory smsCouponHistory) {
        return smsCouponHistoryMapper.insert(smsCouponHistory.setId(idGeneratorUtils.generateId(REDIS_KEY_COUPON_HISTORY)));
    }

    @Override
    public int purchase(Long id) {
        // 根据id查询优惠券详情
        SmsCoupon smsCoupon = smsCouponMapper.selectById(id);
        if (smsCoupon == null) {
            throw new ServiceException("该优惠券已经下架，请选择其他优惠券！");
        }
        // 判断优惠券是否在购买时间
        if (smsCoupon.getReceiveStartTime() == null && DateUtil.compare(smsCoupon.getReceiveStartTime(), DateUtil.date()) > 0) {
            throw new ServiceException("该优惠券活动尚未开始");
        }
        if (smsCoupon.getReceiveEndTime() == null && DateUtil.compare(smsCoupon.getReceiveEndTime(), DateUtil.date()) > 0) {
            throw new ServiceException("该优惠券活动已经结束");
        }
        // 判断优惠券数量是否足够
        if (smsCoupon.getSurplusCount() < 1) {
            throw new ServiceException("该优惠券已被抢光！");
        }
        Long memberId = MemberHolder.get().getId();
        // 获取分布式锁
//        Boolean isLock = redisLockService.tryLock(REDIS_LOCK_COUPON_HISTORY, REDIS_EXPIRE_LOCK);
        // 通过Redisson获取可重入锁
        RLock lock = redisLockService.getRLock(REDIS_LOCK_COUPON_HISTORY);
        // 尝试获取可重入锁
        boolean isLock = lock.tryLock();
        if (!isLock) {
            throw new ServiceException("一人只允许购买一张优惠券");
        }
        try {
            // 校验用户是否已经领取过
            Integer receive = smsCouponHistoryMapper.selectCount(new LambdaQueryWrapper<SmsCouponHistory>()
                    .eq(SmsCouponHistory::getMemberId, memberId)
                    .eq(SmsCouponHistory::getCouponId, id));
            if (receive > 0) {
                throw new ServiceException("您已经领取过该优惠券！");
            }
            // 添加事务
            return transactionTemplate.execute(status -> {
                // 增加领取数量削减库存
                int count = smsCouponMapper.reduce(id);
                if (count <= 0) {
                    throw new ServiceException("该优惠券存量不足！");
                }
                // 新增优惠券订单记录
                SmsCouponHistory smsCouponHistory = SmsCouponHistory.builder()
                        .id(idGeneratorUtils.generateId(REDIS_KEY_COUPON_HISTORY))
                        .memberId(memberId)
                        .couponId(id)
                        .couponCode(smsCoupon.getCode())
                        .getType(2)
                        .payType(0)
                        .build();
                return smsCouponHistoryMapper.insert(smsCouponHistory);
            });
        } finally {
            // 通过lua脚本释放锁解决原子性问题
//            redisLockService.unlockByLua(REDIS_LOCK_COUPON_HISTORY);
            // 释放可重入锁
            lock.unlock();
        }
    }
}
