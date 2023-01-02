package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis操作Service
 * Created by YuanJW on 2022/9/27.
 */
public interface RedisService {
    /**
     * 保存属性
     * @param key
     * @param value
     * @param time
     */
    void set(String key, Object value, long time);

    /**
     * 保存属性
     * @param key
     * @param value
     */
    void set(String key, Object value);

    /**
     * 获取属性
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 删除属性
     * @param key
     * @return
     */
    Boolean del(String key);

    /**
     * 批量删除属性
     * @param keys
     * @return
     */
    Long del(List<String> keys);

    /**
     * 设置过期时间
     * @param key
     * @param time
     * @return
     */
    Boolean expire(String key, Long time);

    /**
     * 获取过期时间
     * @param key
     * @return
     */
    Long getExpire(String key);

    /**
     * 判断是否有该属性
     * @param key
     * @return
     */
    Boolean hasKey(String key);

    /**
     * 按delta递增
     * @param key
     * @param delta
     * @return
     */
    Long incr(String key, Long delta);

    /**
     * 按delta递减
     * @param key
     * @param delta
     * @return
     */
    Long decr(String key, Long delta);

    /**
     * 向Hash结构中放入一个属性
     * @param key
     * @param hashKey
     * @param value
     * @param time
     */
    Boolean hSet(String key, String hashKey, Object value, long time);

    /**
     * 向Hash结构中放入一个属性
     * @param key
     * @param hashKey
     * @param value
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * 获取Hash结构中的属性
     * @param key
     * @param hashKey
     * @return
     */
    Object hGet(String key, String hashKey);

    /**
     * 设置整个Hash结构
     * @param key
     * @param map
     * @param time
     */
    Boolean hSetAll(String key, Map<String, Object> map, long time);

    /**
     * 设置整个Hash结构
     * @param key
     * @param map
     */
    void hSetAll(String key, Map<String, Object> map);

    /**
     * 获取整个Hash结构
     * @param key
     * @return
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * 删除Hash结构中的属性
     * @param key
     * @param hashKey
     */
    void hDel(String key, Object... hashKey);

    /**
     * 判断Hash结构中是否有该属性
     * @param key
     * @param hashKey
     * @return
     */
    Boolean hHashKey(String key, String hashKey);

    /**
     * Hash结构中属性递增
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    Long hIncr(String key, String hashKey, Long delta);

    /**
     * Hash结构中属性递减
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    Long hDecr(String key, String hashKey, Long delta);

    /**
     * 向Set结构中添加属性
     * @param key
     * @param values
     * @return
     */
    Long sAdd(String key, Object... values);

    /**
     * 向Set结构中添加属性
     * @param key
     * @param values
     * @param time
     * @return
     */
    Long sAddForTime(String key, long time, Object... values);

    /**
     * 获取Set结构
     * @param key
     * @return
     */
    Set<Object> sMembers(String key);

    /**
     * 是否为Set中的属性
     * @param key
     * @param value
     * @return
     */
    Boolean sIsMember(String key, Object value);

    /**
     * 获取Set结构的长度
     * @param key
     * @return
     */
    Long sSize(String key);

    /**
     * 删除Set结构中的属性
     * @param key
     * @param values
     * @return
     */
    Long sRemove(String key, Object... values);

    /**
     * 寻找公共交集
     * @param keys
     * @return
     */
    Set<Object> sInter(List<String> keys);

    /**
     * 向ZSet结构中添加属性
     * @param key
     * @param value
     * @param score
     * @return
     */
    Boolean zAdd(String key, Object value, double score);

    /**
     * 获取zSet结构元素的权重
     * @param key
     * @param value
     * @return
     */
    public Double zScore(String key, Object value);

    /**
     * 获取zSet结构中范围的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set zRange(String key, long start, long end);

    /**
     * 删除ZSet结构中的属性
     * @param key
     * @param values
     * @return
     */
    Long zRemove(String key, Object... values);

    /**
     * 向List结构中添加属性
     * @param key
     * @param value
     * @return
     */
    Long lPush(String key, Object value);

    /**
     * 向List结构中添加属性
     * @param key
     * @param value
     * @param time
     * @return
     */
    Long lPush(String key, Object value, long time);

    /**
     * 向List结构中批量添加属性
     * @param key
     * @param values
     * @return
     */
    Long lPushAll(String key, Object... values);

    /**
     * 向List结构中批量添加属性
     * @param key
     * @param time
     * @param values
     * @return
     */
    Long lPushAll(String key, long time, Object ... values);

    /**
     * 根据索引获取List中的属性
     * @param key
     * @param index
     * @return
     */
    Object lIndex(String key, long index);

    /**
     * 获取List结构中的属性
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 获取List结构的长度
     * @param key
     * @return
     */
    Long lSize(String key);

    /**
     * 从List结构中移除属性
     * @param key
     * @param count
     * @param value
     * @return
     */
    Long lRemove(String key, long count, Object value);

    /**
     * Setnx实现锁
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    Boolean tryLock(String key, String value, long timeout);
}
