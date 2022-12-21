package com.example.demo.mapper;

import com.example.demo.entity.SmsReviewCommentReply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* SmsReviewCommentReplyMapper
* Created by YuanJW on 2022-12-19 14:46:03
*/
@Mapper
public interface SmsReviewCommentReplyMapper extends BaseMapper<SmsReviewCommentReply> {
    /**
    * 新增SmsReviewCommentReply对象
    * @param record
    * @return id
    */
    Long saveSmsReviewCommentReply(SmsReviewCommentReply record);
    /**
    * 根据id查询SmsReviewCommentReply对象
    * @param id
    * @return SmsReviewCommentReply
    */
    SmsReviewCommentReply getSmsReviewCommentReplyById(Long id);
    /**
    * 根据搜索条件获取SmsReviewCommentReply列表
    * @param record
    * @return
    */
    List<SmsReviewCommentReply> getSmsReviewCommentReplys(SmsReviewCommentReply record);
    /**
    * 修改SmsReviewCommentReply对象
    * @param record
    * @return
    */
    int updateSmsReviewCommentReply(SmsReviewCommentReply record);
    /**
    * 批量删除SmsReviewCommentReply
    * @param ids
    * @return
    */
    int deleteSmsReviewCommentReplys(List<Long> ids);
    /**
    * 统计SmsReviewCommentReply
    * @param record
    * @return
    */
    int countSmsReviewCommentReply(SmsReviewCommentReply record);
}