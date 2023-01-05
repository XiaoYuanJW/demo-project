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
    * @param smsReviewComment
    * @return id
    */
    Long saveSmsReviewComment(SmsReviewComment smsReviewComment);
    /**
    * 根据id查询SmsReviewComment对象
    * @param id
    * @return SmsReviewComment
    */
    SmsReviewComment getSmsReviewCommentById(Long id);
    /**
    * 根据搜索条件获取SmsReviewComment列表
    * @param smsReviewComment
    * @return
    */
    List<SmsReviewComment> getSmsReviewComments(SmsReviewComment smsReviewComment);
    /**
    * 修改SmsReviewComment对象
    * @param smsReviewComment
    * @return
    */
    int updateSmsReviewComment(SmsReviewComment smsReviewComment);
    /**
    * 批量删除SmsReviewComment
    * @param ids
    * @return
    */
    int deleteSmsReviewComments(List<Long> ids);
    /**
    * 统计SmsReviewComment
    * @param smsReviewComment
    * @return
    */
    int countSmsReviewComment(SmsReviewComment smsReviewComment);
}