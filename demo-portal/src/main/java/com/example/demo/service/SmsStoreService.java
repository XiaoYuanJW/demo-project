package com.example.demo.service;

import com.example.demo.entity.SmsStore;

import java.util.List;

/**
 * 商铺操作接口类
 * Created by YuanJW on 2022/12/6.
 */
public interface SmsStoreService {
    /**
     * 查看商铺详情
     * @param id
     * @return
     * @throws InterruptedException
     */
    SmsStore detail(Long id) throws InterruptedException;

    /**
     * 修改商铺信息
     * @param smsStore
     */
    void update(SmsStore smsStore);

    /**
     * 获取商铺列表
     * @param smsStore
     * @return
     */
    List<SmsStore> list(SmsStore smsStore);
}
