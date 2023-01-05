package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息队列枚举类
 * Created by YuanJW on 2022/12/21.
 */
@Getter
@AllArgsConstructor
public enum QueueEnum {
    /**
     * 优惠券抢购队列
     */
    QUEUE_COUPON_PURCHASE("demo.coupon.direct", "demo.coupon.purchase", "demo.coupon.purchase"),

    /**
     * 签到日志队列
     */
    QUEUE_SIGN_LOG("demo.sign.direct", "demo.sign.log", "demo.sign.log");


    /**
     * 交换机名称
     */
    private final String exchange;

    /**
     * 队列名称
     */
    private final String name;

    /**
     * 路由键
     */
    private final String routeKey;
}
