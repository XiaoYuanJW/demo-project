package com.example.demo.component;

import com.example.demo.dto.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 优惠券订单消息队列相关配置
 * Created by YuanJW on 2023/1/5.
 */
@Configuration
public class CounponHistoryRabbitMqConfig {
    /**
     * 优惠券订单实际消费队列所绑定的交换机
     * @return
     */
    @Bean
    public DirectExchange counponHistoryDirectExchange() {
        return ExchangeBuilder.directExchange(QueueEnum.QUEUE_COUPON_PURCHASE.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 优惠券订单实际消费队列
     * @return
     */
    @Bean
    public Queue counponHistoryQueue() {
        return new Queue(QueueEnum.QUEUE_COUPON_PURCHASE.getName());
    }

    /**
     * 将优惠券订单队列绑定到交换机
     * @return
     */
    @Bean
    public Binding counponHistoryBinding() {
        return BindingBuilder
                .bind(counponHistoryQueue())
                .to(counponHistoryDirectExchange())
                .with(QueueEnum.QUEUE_COUPON_PURCHASE.getRouteKey());
    }
}
