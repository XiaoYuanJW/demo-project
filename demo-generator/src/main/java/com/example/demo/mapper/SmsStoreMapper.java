package com.example.demo.mapper;

import com.example.demo.entity.SmsStore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* SmsStoreMapper
* Created by YuanJW on 2022-12-06 22:08:52
*/
@Mapper
public interface SmsStoreMapper extends BaseMapper<SmsStore> {
    /**
    * 新增SmsStore对象
    * @param smsStore
    * @return id
    */
    Long saveSmsStore(SmsStore smsStore);
    /**
    * 根据id查询SmsStore对象
    * @param id
    * @return SmsStore
    */
    SmsStore getSmsStoreById(Long id);
    /**
    * 根据搜索条件获取SmsStore列表
    * @param smsStore
    * @return
    */
    List<SmsStore> getSmsStores(SmsStore smsStore);
    /**
    * 修改SmsStore对象
    * @param smsStore
    * @return
    */
    int updateSmsStore(SmsStore smsStore);
    /**
    * 批量删除SmsStore
    * @param ids
    * @return
    */
    int deleteSmsStores(List<Long> ids);
    /**
    * 统计SmsStore
    * @param smsStore
    * @return
    */
    int countSmsStore(SmsStore smsStore);
}