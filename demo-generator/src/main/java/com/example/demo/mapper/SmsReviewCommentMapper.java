package com.example.demo.mapper;

import com.example.demo.entity.SmsReviewComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* SmsReviewCommentMapper
* Created by YuanJW on 2022-12-19 14:46:03
*/
@Mapper
public interface SmsReviewCommentMapper extends BaseMapper<SmsReviewComment> {
    /**
    * 新增SmsReviewComment对象
    * @param record
    * @return id
    */
    Long saveSmsReviewComment(SmsReviewComment record);
    /**
    * 根据id查询SmsReviewComment对象
    * @param id
    * @return SmsReviewComment
    */
    SmsReviewComment getSmsReviewCommentById(Long id);
    /**
    * 根据搜索条件获取SmsReviewComment列表
    * @param record
    * @return
    */
    List<SmsReviewComment> getSmsReviewComments(SmsReviewComment record);
    /**
    * 修改SmsReviewComment对象
    * @param record
    * @return
    */
    int updateSmsReviewComment(SmsReviewComment record);
    /**
    * 批量删除SmsReviewComment
    * @param ids
    * @return
    */
    int deleteSmsReviewComments(List<Long> ids);
    /**
    * 统计SmsReviewComment
    * @param record
    * @return
    */
    int countSmsReviewComment(SmsReviewComment record);
}