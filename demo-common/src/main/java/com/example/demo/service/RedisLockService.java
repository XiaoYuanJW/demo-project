package com.example.demo.service;

import org.redisson.api.RLock;

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
    boolean tryLock(String key, long timeout);

    /**
     * 释放分布式锁
     * @param key
     * @return
     */
    void unlock(String key);

    /**
     * 通过lua脚本释放锁
     * @param key
     */
    void unlockByLua(String key);

    /**
     * 通过Redisson获取可重入锁
     * @param key
     * @return
     */
    RLock getRLock(String key);
}
