package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.constants.RedisConstant;
import com.example.demo.constants.SysFileConstant;
import com.example.demo.dto.SearchStoreParam;
import com.example.demo.entity.SmsStore;
import com.example.demo.entity.SysFile;
import com.example.demo.mapper.SmsStoreMapper;
import com.example.demo.service.RedisService;
import com.example.demo.service.SmsStoreService;
import com.example.demo.service.SysFileService;
import com.example.demo.utils.PageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商铺操作实现类
 * Created by YuanJW on 2022/12/6.
 */
@Service
public class SmsStoreServiceImpl extends ServiceImpl<SmsStoreMapper, SmsStore> implements SmsStoreService {
    @Resource
    private SmsStoreMapper smsStoreMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SysFileService sysFileService;
    @Value("${redis.key.store}")
    private String REDIS_KEY_STORE;
    @Value("${redis.lock.store}")
    private String REDIS_LOCK_STORE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE_COMMON;
    @Value("${redis.expire.pierce}")
    private Long REDIS_EXPIRE_PIERCE;
    @Value("${redis.expire.lock}")
    private Long REDIS_EXPIRE_LOCK;
    @Value("${redis.key.geo}")
    private String REDIS_KEY_GEO;

    @Override
    public SmsStore detail(Long id) throws InterruptedException {
        // 从redis中获取商铺信息
        String key = REDIS_KEY_STORE + ":" + id;
        Map<Object, Object> value = redisService.hGetAll(key);
        SmsStore smsStore = null;
        if (MapUtil.isNotEmpty(value)) {
            // 遇到空对象直接返回，防止缓存穿透
            if (RedisConstant.NULL_MAP.equals(value)) {
                return smsStore;
            }
            // 缓存存在直接返回商铺信息
            smsStore = BeanUtil.fillBeanWithMap(value, new SmsStore(), false);
            return smsStore;
        }
        // 获取互斥锁
        String lockKey = REDIS_LOCK_STORE + ":" + id;
        Boolean flag = redisService.tryLock(lockKey, "0", REDIS_EXPIRE_LOCK);
        if (!flag) {
            // 等待并重试
            Thread.sleep(20);
            detail(id);
        }
        // TODO : 获取锁成功后再次检测redis的缓存是否存在（double-check）
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
        } else {
            // 缓存空对象避免缓存穿透
            redisService.hSetAll(key, RedisConstant.NULL_MAP, REDIS_EXPIRE_PIERCE);
        }
        // 释放互斥锁
        redisService.del(lockKey);
        return smsStore;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int update(SmsStore smsStore) {
        SmsStore oldStore = smsStoreMapper.getSmsStoreById(smsStore.getId());
        String key = REDIS_KEY_STORE + ":" + smsStore.getId();
        // 更新数据库
        int count = smsStoreMapper.updateById(smsStore);
        if (count > 0) {
            // 删除缓存
            redisService.del(key);
            // TODO : 修改redis中地理信息
            double x = smsStore.getLat() != null ? smsStore.getLat().doubleValue() : oldStore.getLat().doubleValue();
            double y = smsStore.getLng() != null ? smsStore.getLng().doubleValue() : oldStore.getLng().doubleValue();
            redisTemplate.opsForGeo().remove(REDIS_KEY_GEO + ":" + oldStore.getCategoryId(), oldStore.getId());
            redisTemplate.opsForGeo().add(REDIS_KEY_GEO + ":" + smsStore.getCategoryId(),
                    new Point(x, y),
                    smsStore.getId()
            );
        }
        return count;
    }

    @Override
    public int insert(SmsStore smsStore) {
        // 更新数据库
        int count = smsStoreMapper.insert(smsStore);
        // TODO : 新增地理信息至redis
        if (count > 0) {
            if (smsStore.getLng() != null && smsStore.getLat() != null) {
                // 将店铺地理信息写入redis中
                redisTemplate.opsForGeo()
                        .add(REDIS_KEY_GEO + ":" + smsStore.getCategoryId(),
                                new Point(smsStore.getLat().doubleValue(), smsStore.getLng().doubleValue()),
                                smsStore.getId()
                        );
            }
        }
        return count;
    }

    @Override
    public List<SmsStore> list(SmsStore smsStore) {
        // TODO : 整合ElasticSearch分布式搜索引擎
        return smsStoreMapper.getSmsStores(smsStore);
    }

    @Override
    public void loadStoreGeo() {
        // 查询所有店铺的信息
        List<SmsStore> smsStores = smsStoreMapper.selectList(new LambdaQueryWrapper<>());
        // 按照商铺种类进行分组
        Map<Long, List<SmsStore>> map = smsStores.stream().collect(Collectors.groupingBy(SmsStore::getCategoryId));
        // 将分组结果的地理信息写入Redis
        for (Map.Entry<Long, List<SmsStore>> entry : map.entrySet()) {
            // 获取分组类型的id
            Long categoryKey = entry.getKey();
            // 获取相同类型的店铺列表
            List<SmsStore> stores = entry.getValue();
            for (SmsStore store : stores) {
                // 将店铺地理信息写入redis中
                redisTemplate.opsForGeo()
                        .add(REDIS_KEY_GEO + ":" + categoryKey,
                                new Point(store.getLat().doubleValue(), store.getLng().doubleValue()),
                                store.getId()
                        );
            }
        }
    }

    @Override
    public List<SmsStore> search(SearchStoreParam searchStoreParam) {
        // 是否根据地理坐标进行查询
        if (searchStoreParam.getLat() == null || searchStoreParam.getLng() == null) {
            return smsStoreMapper.selectList(new LambdaQueryWrapper<SmsStore>()
                    .eq(SmsStore::getCategoryId, searchStoreParam.getCategoryId()));
        }
        // 从redis中获取地理距离排序的分页结果
        int from = (PageUtils.getPage().getPageNum() - 1) * PageUtils.getPage().getPageSize();
        int count = from + PageUtils.getPage().getPageSize();
        GeoResults<RedisGeoCommands.GeoLocation<Object>> search = redisTemplate.opsForGeo()
                .search(
                        REDIS_KEY_GEO + ":" + searchStoreParam.getCategoryId(),
                        GeoReference.fromCoordinate(searchStoreParam.getLat().doubleValue(), searchStoreParam.getLng().doubleValue()),
                        new Distance(searchStoreParam.getDistance() != null ? searchStoreParam.getDistance() : 5000),
                        RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeDistance().limit(count)
                );
        List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> content = search.getContent();
        if (content == null || content.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> storeIds = new ArrayList<>(content.size());
        Map<Long, Double> distanceMap = new HashMap<>(content.size());
        content.stream()
                // 跳过前页的结果
                .skip(from)
                .forEach(geoLocationGeoResult -> {
                    Long storeId = Long.valueOf(geoLocationGeoResult.getContent().getName().toString());
                    storeIds.add(storeId);
                    // 将商铺id和商铺距离存储至Map集合
                    distanceMap.put(storeId, geoLocationGeoResult.getDistance().getValue());
                }
        );
        if (storeIds == null || storeIds.isEmpty()) {
            return Collections.emptyList();
        }
        // 根据id集合顺序获取商铺信息
        String join = StrUtil.join(",", storeIds);
        List<SmsStore> smsStores = smsStoreMapper.selectList(new LambdaQueryWrapper<SmsStore>()
                .in(SmsStore::getId, storeIds)
                .last("ORDER BY FIELD(id," + join + ")"));
        smsStores.forEach(smsStore -> {
            // 迭代绑定商铺距离
            smsStore.setDistance(distanceMap.get(smsStore.getId()));
        });
        return smsStores;
    }
}
