package com.example.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置类
 * Created by YuanJW on 2022/12/4.
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.database}")
    private String database;
    @Value("${spring.redis.password}")
    private String password;

    private static final String SCHEMA_PREFIX = "redis://";

    @Bean
    public RedissonClient redissonClient() {
        // 创建配置类
        Config config = new Config();
        // 添加配置
        config.useSingleServer()
                .setAddress(SCHEMA_PREFIX + host + ":" + port)
                .setPassword(password)
                .setTimeout(3000)
                .setPingConnectionInterval(30000)
                .setDatabase(Integer.parseInt(database));
        // 创建Redisson对象
        return Redisson.create(config);
    }
}
