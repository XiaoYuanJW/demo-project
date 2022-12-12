package com.example.demo.service.impl;

import com.example.demo.service.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁操作实现类
 * Created by YuanJW on 2022/12/12.
 */
@Slf4j
@Service
public class RedisLockServiceImpl implements RedisLockService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String KEY_PREFIX = "lock:";

    @Override
    public Boolean tryLock(String key, long timeout) {
        // 获取线程标识
        long threadId = Thread.currentThread().getId();
        return redisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + key, String.valueOf(threadId), timeout, TimeUnit.SECONDS);
    }

    @Override
    public Boolean unlock(String key) {
        return redisTemplate.delete(KEY_PREFIX + key);
    }
}
