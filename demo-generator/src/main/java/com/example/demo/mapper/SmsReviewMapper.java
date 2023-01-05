package com.example.demo.mapper;

import com.example.demo.entity.SmsReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* SmsReviewMapper
* Created by YuanJW on 2022-12-19 15:43:12
*/
@Mapper
public interface SmsReviewMapper extends BaseMapper<SmsReview> {
    /**
    * 新增SmsReview对象
    * @param smsReview
    * @return id
    */
    Long saveSmsReview(SmsReview smsReview);
    /**
    * 根据id查询SmsReview对象
    * @param id
    * @return SmsReview
    */
    SmsReview getSmsReviewById(Long id);
    /**
    * 根据搜索条件获取SmsReview列表
    * @param smsReview
    * @return
    */
    List<SmsReview> getSmsReviews(SmsReview smsReview);
    /**
    * 修改SmsReview对象
    * @param smsReview
    * @return
    */
    int updateSmsReview(SmsReview smsReview);
    /**
    * 批量删除SmsReview
    * @param ids
    * @return
    */
    int deleteSmsReviews(List<Long> ids);
    /**
    * 统计SmsReview
    * @param smsReview
    * @return
    */
    int countSmsReview(SmsReview smsReview);
}