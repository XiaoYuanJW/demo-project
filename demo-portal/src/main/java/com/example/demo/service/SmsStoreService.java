package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.SearchStoreParam;
import com.example.demo.entity.SmsStore;

import java.util.List;

/**
 * 商铺操作接口类
 * Created by YuanJW on 2022/12/6.
 */
public interface SmsStoreService extends IService<SmsStore> {
    /**
     * 查看商铺详情
     * @param id
     * @return
     * @throws InterruptedException
     */
    SmsStore detail(Long id) throws InterruptedException;

    /**
     * 新增商铺信息
     * @param smsStore
     * @return
     */
    int insert(SmsStore smsStore);

    /**
     * 修改商铺信息
     * @param smsStore
     */
    int update(SmsStore smsStore);

    /**
     * 获取商铺列表
     * @param smsStore
     * @return
     */
    List<SmsStore> list(SmsStore smsStore);

    /**
     * 店铺地理预热
     */
    void loadStoreGeo();

    /**
     * 查询附近店铺
     * @param searchStoreParam
     * @return
     */
    List<SmsStore> search(SearchStoreParam searchStoreParam);
}
