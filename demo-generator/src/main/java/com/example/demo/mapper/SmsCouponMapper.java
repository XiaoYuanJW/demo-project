package com.example.demo.mapper;

import com.example.demo.entity.SmsCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* SmsCouponMapper
* Created by YuanJW on 2022-12-09 17:43:39
*/
@Mapper
public interface SmsCouponMapper extends BaseMapper<SmsCoupon> {
    /**
    * 新增SmsCoupon对象
    * @param smsCoupon
    * @return id
    */
    Long saveSmsCoupon(SmsCoupon smsCoupon);
    /**
    * 根据id查询SmsCoupon对象
    * @param id
    * @return SmsCoupon
    */
    SmsCoupon getSmsCouponById(Long id);
    /**
    * 根据搜索条件获取SmsCoupon列表
    * @param smsCoupon
    * @return
    */
    List<SmsCoupon> getSmsCoupons(SmsCoupon smsCoupon);
    /**
    * 修改SmsCoupon对象
    * @param smsCoupon
    * @return
    */
    int updateSmsCoupon(SmsCoupon smsCoupon);
    /**
    * 批量删除SmsCoupon
    * @param ids
    * @return
    */
    int deleteSmsCoupons(List<Long> ids);
    /**
    * 统计SmsCoupon
    * @param smsCoupon
    * @return
    */
    int countSmsCoupon(SmsCoupon smsCoupon);
    /**
     * 更新库存
     * @return
     */
    int reduce(Long id);
}