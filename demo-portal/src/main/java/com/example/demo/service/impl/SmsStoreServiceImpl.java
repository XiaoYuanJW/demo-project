package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.example.demo.constants.SysFileConstant;
import com.example.demo.entity.SmsStore;
import com.example.demo.entity.SysFile;
import com.example.demo.mapper.SmsStoreMapper;
import com.example.demo.service.RedisService;
import com.example.demo.service.SmsStoreService;
import com.example.demo.service.SysFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商铺操作实现类
 * Created by YuanJW on 2022/12/6.
 */
@Service
public class SmsStoreServiceImpl implements SmsStoreService {
    @Resource
    private SmsStoreMapper smsStoreMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private SysFileService sysFileService;
    @Value("${redis.key.store}")
    private String REDIS_KEY_STORE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE_COMMON;

    @Override
    public SmsStore detail(Long id) {
        // 从redis中获取商铺信息
        String key = REDIS_KEY_STORE + ":" + id;
        Map<Object, Object> value = redisService.hGetAll(key);
        SmsStore smsStore = null;
        if (MapUtil.isNotEmpty(value)) {
            // 缓存存在直接返回商铺信息
            smsStore = BeanUtil.fillBeanWithMap(value, new SmsStore(), false);
            // 刷新商铺信息缓存过期时间
            redisService.expire(key, REDIS_EXPIRE_COMMON);
            return smsStore;
        }
        // 从数据库中获取商铺信息
        smsStore = smsStoreMapper.getSmsStoreById(id);
        if (smsStore != null) {
            // 加载文件信息
            List<SysFile> fileList = sysFileService.getFileList(smsStore.getImage());
            Map<String, Object> image = MapUtil.builder(new HashMap<String, Object>()).put(SysFileConstant.Store.IMAGE, fileList).build();
            smsStore.setFiles(image);
            // 数据库存在存入redis缓存
            Map<String, Object> map = BeanUtil.beanToMap(smsStore);
            redisService.hSetAll(key, map, REDIS_EXPIRE_COMMON);
        }
        return smsStore;
    }
}
