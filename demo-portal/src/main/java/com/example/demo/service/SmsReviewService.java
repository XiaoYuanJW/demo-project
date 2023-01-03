package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.ScrollResult;
import com.example.demo.dto.SmsReviewDetail;
import com.example.demo.entity.SmsReview;

import java.util.List;

/**
 * 商铺点评接口
 * Created by YuanJW on 2022/12/19.
 */
public interface SmsReviewService extends IService<SmsReview> {
    /**
     * 新增商铺点评信息
     * @param smsReview
     * @return
     */
    int insert(SmsReview smsReview);

    /**
     * 分页查询商铺点评信息
     * @param smsReview
     * @return
     */
    List<SmsReviewDetail> page(SmsReview smsReview);

    /**
     * 查看商铺点评详情
     * @param id
     * @return
     */
    SmsReviewDetail detail(Long id)  throws InterruptedException;

    /**
     * 给商铺点评进行点赞
     * @param id
     * @return
     */
    boolean like(Long id);

    /**
     * 关注人博客推送
     * @param max
     * @param offSet
     * @return
     */
    ScrollResult follow(Long max, Integer offSet) throws InterruptedException;
}
