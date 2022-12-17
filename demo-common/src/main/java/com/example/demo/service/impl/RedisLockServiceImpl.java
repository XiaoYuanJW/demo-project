package com.example.demo.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.example.demo.service.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;
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
    @Resource
    private RedissonClient redissonClient;

    private static final String KEY_PREFIX = "lock:";

    private static final String VALUE_PREFIX = UUID.randomUUID().toString() + "-";

    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    // 静态代码块提前加载lua脚本，减少每次方法执行的io消耗
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<Long>();
        // 设置文件位置
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("/lua/unlock.lua"));
        // 设置返回值类型
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    @Override
    public boolean tryLock(String key, long timeout) {
        // 获取线程标识
        long threadId = Thread.currentThread().getId();
        Boolean isLock = redisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + key, VALUE_PREFIX + threadId, timeout, TimeUnit.SECONDS);
        return BooleanUtil.isFalse(isLock);
    }

    @Override
    public void unlock(String key) {
        // 获取线程标识
        String value = VALUE_PREFIX + Thread.currentThread().getId();
        // 校验线程和锁的标识是否一致
        if (value.equals(redisTemplate.opsForValue().get(KEY_PREFIX + key))) {
            redisTemplate.delete(KEY_PREFIX + key);
        }
    }

    @Override
    public void unlockByLua(String key) {
        // 获取线程标识
        String value = VALUE_PREFIX + Thread.currentThread().getId();
        // 执行lua脚本
        redisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(KEY_PREFIX + key), value);
    }

    @Override
    public RLock getRLock(String key) {
        return redissonClient.getLock(KEY_PREFIX + key);
    }
}
