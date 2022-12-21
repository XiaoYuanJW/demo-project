package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.dto.SmsReviewDetail;
import com.example.demo.entity.SmsReview;
import com.example.demo.entity.SmsStore;
import com.example.demo.entity.UmsMember;
import com.example.demo.mapper.SmsReviewMapper;
import com.example.demo.service.SmsReviewService;
import com.example.demo.service.SmsStoreService;
import com.example.demo.service.SysFileService;
import com.example.demo.service.UmsMemberService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * 商铺点评接口实现类
 * Created by YuanJW on 2022/12/19.
 */
@Service
public class SmsReviewServiceImpl implements SmsReviewService {
    @Resource
    private SmsReviewMapper smsReviewMapper;
    @Resource
    private SysFileService sysFileService;
    @Resource
    private SmsStoreService smsStoreService;
    @Resource
    private UmsMemberService umsMemberService;

    @Override
    public int insert(SmsReview smsReview) {
        return smsReviewMapper.insert(smsReview);
    }

    @Override
    public SmsReviewDetail detail(Long id) throws InterruptedException {
        SmsReview smsReview = smsReviewMapper.getSmsReviewById(id);
        Assert.notNull(smsReview, "该商铺点评信息不存在");
        smsReview.setPicInfo(sysFileService.getFileList(smsReview.getPic()));
        SmsReviewDetail smsReviewDetail = new SmsReviewDetail();
        BeanUtil.copyProperties(smsReview, smsReviewDetail, false);
        // 获取商铺和用户信息
        SmsStore smsStore = smsStoreService.detail(smsReview.getStoreId());
        smsReviewDetail.setSmsStore(smsStore);
        UmsMember umsMember = umsMemberService.detail(smsReview.getMemberId());
        smsReviewDetail.setUmsMember(umsMember);
        return smsReviewDetail;
    }

}
