package com.example.demo.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.dto.QueueEnum;
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
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${redis.key.couponHistory}")
    private String REDIS_KEY_COUPON_HISTORY;
    @Value("${redis.lock.couponHistory}")
    private String REDIS_LOCK_COUPON_HISTORY;
    @Value("${redis.expire.lock}")
    private Long REDIS_EXPIRE_LOCK;
    private static final DefaultRedisScript<Long> SECKILL_LOCK;
    @Resource
    private AmqpTemplate amqpTemplate;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    static {
        SECKILL_LOCK = new DefaultRedisScript<Long>();
        SECKILL_LOCK.setLocation(new ClassPathResource("/lua/seckill.lua"));
        SECKILL_LOCK.setResultType(Long.class);
    }
    // 阻塞队列
    private BlockingQueue<SmsCouponHistory> COUPON_HISTORY_QUEUE = new ArrayBlockingQueue<>(1024 * 1024);

    /**
     * 初始化执行开启线程池执行阻塞队列
     */
    @PostConstruct
    private void init() {
        threadPoolTaskExecutor.submit(new CounponHistoryHandler());
    }

    /**
     *
     */
    private class CounponHistoryHandler implements Runnable {
        @Override
        public void run() {
            // 死循环：take()从阻塞队列中获取队列中的对象，取不到的时候会自己阻塞
            while (true) {
                try {
                    // 从阻塞队列中获取订单信息
                    SmsCouponHistory smsCouponHistory = COUPON_HISTORY_QUEUE.take();
                    RLock lock = redisLockService.getRLock(REDIS_LOCK_COUPON_HISTORY);
                    boolean isLock = lock.tryLock();
                    if (!isLock) {
                        throw new ServiceException("一人只允许购买一张优惠券");
                    }
                    try {
                        // 调用事务管理
                        threadPoolTaskExecutor.submit(() -> {
                            transactionTemplate.execute(status -> {
                                // 增加领取数量削减库存
                                int count = smsCouponMapper.reduce(smsCouponHistory.getCouponId());
                                // 新增优惠券订单记录
                                smsCouponHistoryMapper.insert(smsCouponHistory);
                                return smsCouponHistory.getId().longValue();
                            });
                        });
                    } finally {
                        lock.unlock();
                    }
                } catch (Exception e) {
                    throw new ServiceException("订单异常！");
                }
            }
        }
    }

    @Override
    public int insert(SmsCouponHistory smsCouponHistory) {
        return smsCouponHistoryMapper.insert(smsCouponHistory.setId(idGeneratorUtils.generateId(REDIS_KEY_COUPON_HISTORY)));
    }

    @Override
    public long purchase(Long id) {
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
                // 新增优惠券订单记录
                SmsCouponHistory smsCouponHistory = SmsCouponHistory.builder()
                        .id(idGeneratorUtils.generateId(REDIS_KEY_COUPON_HISTORY))
                        .memberId(memberId)
                        .couponId(id)
                        .couponCode(smsCoupon.getCode())
                        .getType(2)
                        .payType(0)
                        .build();
                smsCouponHistoryMapper.insert(smsCouponHistory);
                return smsCouponHistory.getId().longValue();
            });
        } finally {
            // 通过lua脚本释放锁解决原子性问题
//            redisLockService.unlockByLua(REDIS_LOCK_COUPON_HISTORY);
            // 释放可重入锁
            lock.unlock();
        }
    }

    @Override
    public long asyncPurchase(Long id) {
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
        Long memberId = MemberHolder.get().getId();
        // 执行Lua脚本
        Long result = redisTemplate.execute(
                SECKILL_LOCK,
                Collections.emptyList(),
                id, memberId
        );
        // 没有购买资格返回抢购失败
        if (result.intValue() == -1) {
            throw new ServiceException("优惠券库存不足");
        } else if (result.intValue() == -2) {
            throw new ServiceException("用户不能重复下单");
        }
        long historyId = idGeneratorUtils.generateId(REDIS_KEY_COUPON_HISTORY);
        // 将订单信息添加到阻塞队列中执行 TODO: RabbitMq的异步操作
        SmsCouponHistory smsCouponHistory = SmsCouponHistory.builder()
                .id(idGeneratorUtils.generateId(REDIS_KEY_COUPON_HISTORY))
                .memberId(memberId)
                .couponId(id)
                .couponCode(smsCoupon.getCode())
                .getType(2)
                .payType(0)
                .build();
        // 使用RabbitMQ异步处理订单
        amqpTemplate.convertAndSend(
                QueueEnum.QUEUE_COUPON_PURCHASE.getExchange(),
                QueueEnum.QUEUE_COUPON_PURCHASE.getRouteKey(),
                smsCouponHistory
        );
        // 使用阻塞队列异步处理订单
//        COUPON_HISTORY_QUEUE.add(smsCouponHistory);
        // 返回订单id
        return historyId;
    }

    @RabbitListener(queues = "demo.coupon.purchase")
    public void handleConponHistory(SmsCouponHistory smsCouponHistory) {
        // 从阻塞队列中获取订单信息
        RLock lock = redisLockService.getRLock(REDIS_LOCK_COUPON_HISTORY);
        boolean isLock = lock.tryLock();
        if (!isLock) {
            throw new ServiceException("一人只允许购买一张优惠券");
        }
        try {
            // 调用事务管理
            threadPoolTaskExecutor.submit(() -> {
                transactionTemplate.execute(status -> {
                    // 增加领取数量削减库存
                    int count = smsCouponMapper.reduce(smsCouponHistory.getCouponId());
                    // 新增优惠券订单记录
                    smsCouponHistoryMapper.insert(smsCouponHistory);
                    return smsCouponHistory.getId().longValue();
                });
            });
        } finally {
            lock.unlock();
        }
    }
}
