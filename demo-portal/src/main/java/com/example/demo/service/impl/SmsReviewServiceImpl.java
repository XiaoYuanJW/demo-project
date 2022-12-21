package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.SmsReviewDetail;
import com.example.demo.entity.SmsReview;
import com.example.demo.entity.SmsStore;
import com.example.demo.entity.UmsMember;
import com.example.demo.mapper.SmsReviewMapper;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商铺点评接口实现类
 * Created by YuanJW on 2022/12/19.
 */
@Service
public class SmsReviewServiceImpl extends ServiceImpl<SmsReviewMapper, SmsReview> implements SmsReviewService {
    @Resource
    private SmsReviewMapper smsReviewMapper;
    @Resource
    private SysFileService sysFileService;
    @Resource
    private SmsStoreService smsStoreService;
    @Resource
    private UmsMemberService umsMemberService;
    @Resource
    private RedisService redisService;
    @Value("${redis.key.reviewLike}")
    private String reviewLike;

    @Override
    public int insert(SmsReview smsReview) {
        return smsReviewMapper.insert(smsReview);
    }

    @Override
    public List<SmsReviewDetail> page(SmsReview smsReview) {
        List<SmsReview> smsReviews = smsReviewMapper.getSmsReviews(smsReview);
        List<SmsReviewDetail> smsReviewDetailList = BeanUtil.copyToList(smsReviews, SmsReviewDetail.class);
        smsReviewDetailList.forEach(smsReviewDetail -> {
            smsReviewDetail.setPicInfo(sysFileService.getFileList(smsReviewDetail.getPic()));
            // 获取商铺和用户信息
            SmsStore smsStore = null;
            try {
                smsStore = smsStoreService.detail(smsReviewDetail.getStoreId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            smsReviewDetail.setSmsStore(smsStore);
            UmsMember umsMember = umsMemberService.detail(smsReviewDetail.getMemberId());
            smsReviewDetail.setUmsMember(umsMember);
            // 获取当前用户的点评点赞状态
            smsReviewDetail.setIsLike(BooleanUtil.isTrue(isLike(smsReviewDetail.getId())));
        });
        return smsReviewDetailList;
    }

    @Override
    public SmsReviewDetail detail(Long id) throws InterruptedException {
        SmsReview smsReview = smsReviewMapper.getSmsReviewById(id);
        Assert.notNull(smsReview, "该商铺点评信息不存在");
        smsReview.setPicInfo(sysFileService.getFileList(smsReview.getPic()));
        SmsReviewDetail smsReviewDetail = new SmsReviewDetail();
        BeanUtil.copyProperties(smsReview, smsReviewDetail, false);
        // 获取商铺和用户信息
        SmsStore smsStore = smsStoreService.detail(smsReviewDetail.getStoreId());
        smsReviewDetail.setSmsStore(smsStore);
        UmsMember umsMember = umsMemberService.detail(smsReviewDetail.getMemberId());
        smsReviewDetail.setUmsMember(umsMember);
        // 获取当前用户的点评点赞状态
        smsReviewDetail.setIsLike(isLike(id));
        return smsReviewDetail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean like(Long id) {
        // 从ThreadLocal中获取当前用户id
        Long memberId = umsMemberService.info().getId();
        boolean flag;
        // 判断点赞状态
        if (BooleanUtil.isTrue(isLike(id))) {
            // 点评的点赞数量 - 1
            flag = this.update().setSql("likes = likes - 1").eq("id", id).update();
            // 数据库更新成功后操作redis
            if (flag) {
                redisService.sRemove(reviewLike + ":" + id, memberId);
            }
            return flag;
        }
        // 点评的点赞数量 + 1
        flag = this.update().setSql("likes = likes + 1").eq("id", id).update();
        if (flag) {
            // 将用户的点赞信息持久化到redis中的set结构中
            redisService.sAdd(reviewLike + ":" + id, memberId);
        }
        return flag;
    }

    /**
     * 判断用户是否被点赞
     * @param id
     * @return
     */
    private Boolean isLike(Long id) {
        // 从ThreadLocal中获取当前用户id
        Long memberId = umsMemberService.info().getId();
        // 判断该用户的点赞信息是否存在于Redis的set结构中
        return redisService.sIsMember(reviewLike + ":" + id, memberId);
    }
}
