package com.example.demo.service;

import com.example.demo.entity.SmsStore;

/**
 * 商铺操作接口类
 * Created by YuanJW on 2022/12/6.
 */
public interface SmsStoreService {
    /**
     * 查看商铺详情
     * @param id
     * @return
     */
    SmsStore detail(Long id);
}
