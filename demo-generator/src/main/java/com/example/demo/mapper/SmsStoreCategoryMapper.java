package com.example.demo.mapper;

import com.example.demo.entity.SmsStoreCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* SmsStoreCategoryMapper
* Created by YuanJW on 2022-12-08 13:43:36
*/
@Mapper
public interface SmsStoreCategoryMapper extends BaseMapper<SmsStoreCategory> {
    /**
    * 新增SmsStoreCategory对象
    * @param smsStoreCategory
    * @return id
    */
    Long saveSmsStoreCategory(SmsStoreCategory smsStoreCategory);
    /**
    * 根据id查询SmsStoreCategory对象
    * @param id
    * @return SmsStoreCategory
    */
    SmsStoreCategory getSmsStoreCategoryById(Long id);
    /**
    * 根据搜索条件获取SmsStoreCategory列表
    * @param smsStoreCategory
    * @return
    */
    List<SmsStoreCategory> getSmsStoreCategorys(SmsStoreCategory smsStoreCategory);
    /**
    * 修改SmsStoreCategory对象
    * @param smsStoreCategory
    * @return
    */
    int updateSmsStoreCategory(SmsStoreCategory smsStoreCategory);
    /**
    * 批量删除SmsStoreCategory
    * @param ids
    * @return
    */
    int deleteSmsStoreCategorys(List<Long> ids);
    /**
    * 统计SmsStoreCategory
    * @param smsStoreCategory
    * @return
    */
    int countSmsStoreCategory(SmsStoreCategory smsStoreCategory);
}