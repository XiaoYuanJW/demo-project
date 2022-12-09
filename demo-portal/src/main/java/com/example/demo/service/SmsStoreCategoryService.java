package com.example.demo.service;

import com.example.demo.entity.SmsStoreCategory;

import java.util.List;

/**
 * 商铺种类操作类
 * Created by YuanJW on 2022/12/8.
 */
public interface SmsStoreCategoryService {
    /**
     * 获取商铺种类详情
     * @param id
     * @return
     */
    SmsStoreCategory detail(Long id);

    /**
     * 修改商铺信息
     * @param smsStoreCategory
     */
    void update(SmsStoreCategory smsStoreCategory);

    /**
     * 获取商铺种类列表
     * @param smsStoreCategory
     * @return
     */
    List<SmsStoreCategory> list(SmsStoreCategory smsStoreCategory);
}
