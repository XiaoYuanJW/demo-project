package com.example.demo.service.impl;

import cn.hutool.core.date.DateUtil;
import com.example.demo.entity.SmsCoupon;
import com.example.demo.entity.SmsCouponHistory;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.SmsCouponHistoryMapper;
import com.example.demo.mapper.SmsCouponMapper;
import com.example.demo.service.SmsCouponHistoryService;
import com.example.demo.util.MemberHolder;
import com.example.demo.utils.IdGeneratorUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Value("${redis.key.couponHistory}")
    private String REDIS_KEY_COUPON_HISTORY;

    @Override
    public int insert(SmsCouponHistory smsCouponHistory) {
        return smsCouponHistoryMapper.insert(smsCouponHistory.setId(idGeneratorUtils.generateId(REDIS_KEY_COUPON_HISTORY)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
        // 增加领取数量削减库存
        smsCouponMapper.updateById(smsCoupon.setSurplusCount(smsCoupon.getSurplusCount() - 1)
                .setReceiveCount(smsCoupon.getReceiveCount() + 1));
        // 新增优惠券订单记录
        SmsCouponHistory smsCouponHistory = SmsCouponHistory.builder()
                .id(idGeneratorUtils.generateId(REDIS_KEY_COUPON_HISTORY))
                .memberId(MemberHolder.get().getId())
                .couponId(id)
                .couponCode(smsCoupon.getCode())
                .getType(2)
                .payType(0)
                .build();
        return smsCouponHistoryMapper.insert(smsCouponHistory);
    }
}
