package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.SmsCouponHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* SmsCouponHistoryMapper
* Created by YuanJW on 2022-12-09 15:33:39
*/
@Mapper
public interface SmsCouponHistoryMapper extends BaseMapper<SmsCouponHistory> {
    /**
    * 新增SmsCouponHistory对象
    * @param smsCouponHistory
    * @return id
    */
    Long saveSmsCouponHistory(SmsCouponHistory smsCouponHistory);
    /**
    * 根据id查询SmsCouponHistory对象
    * @param id
    * @return SmsCouponHistory
    */
    SmsCouponHistory getSmsCouponHistoryById(Long id);
    /**
    * 根据搜索条件获取SmsCouponHistory列表
    * @param smsCouponHistory
    * @return
    */
    List<SmsCouponHistory> getSmsCouponHistorys(SmsCouponHistory smsCouponHistory);
    /**
    * 修改SmsCouponHistory对象
    * @param smsCouponHistory
    * @return
    */
    int updateSmsCouponHistory(SmsCouponHistory smsCouponHistory);
    /**
    * 批量删除SmsCouponHistory
    * @param ids
    * @return
    */
    int deleteSmsCouponHistorys(List<Long> ids);
    /**
    * 统计SmsCouponHistory
    * @param smsCouponHistory
    * @return
    */
    int countSmsCouponHistory(SmsCouponHistory smsCouponHistory);
}