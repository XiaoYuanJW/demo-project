package com.example.demo.service;

/**
 * Redis分布式锁操作接口类
 * Created by YuanJW on 2022/12/12.
 */
public interface RedisLockService {
    /**
     * 获取分布式锁
     * @param key
     * @param timeout
     * @return
     */
    Boolean tryLock(String key, long timeout);

    /**
     * 释放分布式锁
     * @param key
     * @return
     */
    void unlock(String key);
}
