package com.example.demo.component;

import com.example.demo.dto.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 签到消息队列相关配置
 * Created by YuanJW on 2023/1/5.
 */
@Configuration
public class SignRabbitMqConfig {
    /**
     * 签到消息实际消费队列所绑定的交换机
     * @return
     */
    @Bean
    public DirectExchange signDirectExchange() {
        return ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_SIGN_LOG.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 签到实际消费队列
     * @return
     */
    @Bean
    public Queue signQueue() {
        return new Queue(QueueEnum.QUEUE_SIGN_LOG.getName());
    }


    /**
     * 将签到队列绑定到交换机
     */
    @Bean
    public Binding signBinding(DirectExchange signDirectExchange, Queue signQueue){
        return BindingBuilder
                .bind(signQueue)
                .to(signDirectExchange)
                .with(QueueEnum.QUEUE_SIGN_LOG.getRouteKey());
    }
}
