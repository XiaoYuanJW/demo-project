package com.example.demo.service;

import com.example.demo.dto.SmsReviewDetail;
import com.example.demo.entity.SmsReview;

/**
 * 商铺点评接口
 * Created by YuanJW on 2022/12/19.
 */
public interface SmsReviewService {
    /**
     * 新增商铺点评信息
     * @param smsReview
     * @return
     */
    int insert(SmsReview smsReview);

    /**
     * 查看商铺点评详情
     * @param id
     * @return
     */
    SmsReviewDetail detail(Long id)  throws InterruptedException ;
}
