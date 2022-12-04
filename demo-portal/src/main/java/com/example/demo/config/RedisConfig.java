package com.example.demo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置类
 * Created by YuanJW on 2022/12/4.
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {
}
