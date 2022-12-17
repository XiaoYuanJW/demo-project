package com.example.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.JSONObject;
import com.example.demo.constants.RedisConstant;
import com.example.demo.dto.CommonCache;
import com.example.demo.entity.SmsStoreCategory;
import com.example.demo.entity.SysFile;
import com.example.demo.mapper.SmsStoreCategoryMapper;
import com.example.demo.service.RedisService;
import com.example.demo.service.SmsStoreCategoryService;
import com.example.demo.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 商铺种类操作实现类
 * Created by YuanJW on 2022/12/8.
 */
@Slf4j
@Service
public class SmsStoreCategoryServiceImpl implements SmsStoreCategoryService {
    @Resource
    private SmsStoreCategoryMapper smsStoreCategoryMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private SysFileService sysFileService;
    @Resource
    private Executor threadPoolTaskExecutor;
    @Value("${redis.key.storeCategory}")
    private String REDIS_KEY_STORE_CATEGORY;
    @Value("${redis.lock.storeCategory}")
    private String REDIS_LOCK_STORE_CATEGORY;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE_COMMON;
    @Value("${redis.expire.lock}")
    private Long REDIS_EXPIRE_LOCK;
    @Value("${redis.expire.logical}")
    private Long REDIS_EXPIRE_LOGICAL;

    @Override
    public SmsStoreCategory detail(Long id) {
        String key = REDIS_KEY_STORE_CATEGORY + ":" + id;
        String jsonCategory = (String) redisService.get(key);
        SmsStoreCategory smsStoreCategory = null;
        if (StrUtil.isNotEmpty(jsonCategory)) {
            if (RedisConstant.NULL_VALUE.equals(jsonCategory)) {
                // 遇到空对象直接返回，防止缓存穿透
                return smsStoreCategory;
            }
            // 缓存命中
            CommonCache<JSONObject> commonCache = JSONUtil.toBean(jsonCategory, CommonCache.class);
            smsStoreCategory = JSONUtil.toBean(commonCache.getData(), SmsStoreCategory.class);
            LocalDateTime expireTime = commonCache.getExpireTime();
            // 判断是否过期
            if (expireTime.isAfter(LocalDateTime.now())) {
                return smsStoreCategory;
            }
            // 过期则要进行缓存重建
            String lockKey = REDIS_LOCK_STORE_CATEGORY + ":" + id;
            // 获取互斥锁
            Boolean flag = redisService.tryLock(lockKey, "0", REDIS_EXPIRE_LOCK);
            if (flag) {
                threadPoolTaskExecutor.execute(() -> {
                    // 重建缓存
                    SmsStoreCategory category = smsStoreCategoryMapper.selectById(id);
                    if (category != null) {
                        List<SysFile> fileList = sysFileService.getFileList(category.getImage());
                        category.setImageInfo(fileList);
                        CommonCache cache = CommonCache.builder()
                                .expireTime(LocalDateTime.now().minusSeconds(REDIS_EXPIRE_LOGICAL))
                                .data(category)
                                .build();
                        redisService.set(key, JSONUtil.toJsonStr(cache));
                    } else {
                        redisService.set(key, RedisConstant.NULL_VALUE, REDIS_EXPIRE_COMMON);
                    }
                    // 释放互斥锁
                    redisService.del(lockKey);
                });
            }
            return smsStoreCategory;
        }
        smsStoreCategory = smsStoreCategoryMapper.selectById(id);
        if (smsStoreCategory != null) {
            List<SysFile> fileList = sysFileService.getFileList(smsStoreCategory.getImage());
            smsStoreCategory.setImageInfo(fileList);
            CommonCache cache = CommonCache.builder()
                    .expireTime(LocalDateTime.now().minusSeconds(REDIS_EXPIRE_LOGICAL))
                    .data(smsStoreCategory)
                    .build();
            redisService.set(key, JSONUtil.toJsonStr(cache));
        } else {
            redisService.set(key, RedisConstant.NULL_VALUE, REDIS_EXPIRE_COMMON);
        }
        return smsStoreCategory;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(SmsStoreCategory smsStoreCategory) {
        String key = REDIS_KEY_STORE_CATEGORY + ":" + smsStoreCategory.getId();
        smsStoreCategoryMapper.updateById(smsStoreCategory);
        redisService.del(key);
    }

    @Override
    public List<SmsStoreCategory> list(SmsStoreCategory smsStoreCategory) {
        return smsStoreCategoryMapper.getSmsStoreCategorys(smsStoreCategory);
    }
}
