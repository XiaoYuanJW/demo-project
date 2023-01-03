package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.MemberDto;
import com.example.demo.dto.ScrollResult;
import com.example.demo.dto.SmsReviewDetail;
import com.example.demo.entity.SmsReview;
import com.example.demo.entity.SmsStore;
import com.example.demo.entity.UmsFollow;
import com.example.demo.entity.UmsMember;
import com.example.demo.mapper.SmsReviewMapper;
import com.example.demo.mapper.UmsFollowMapper;
import com.example.demo.service.*;
import com.example.demo.util.MemberHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商铺点评接口实现类
 * Created by YuanJW on 2022/12/19.
 */
@Slf4j
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
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UmsFollowMapper umsFollowMapper;
    @Value("${redis.key.reviewLike}")
    private String reviewLike;
    @Value("${redis.key.reviewPush}")
    private String reviewPush;
    @Value("${redis.key.follow}")
    private String follewKey;

    @Override
    public int insert(SmsReview smsReview) {
        Long id = MemberHolder.get().getId();
        smsReview.setMemberId(id);
        int insert = smsReviewMapper.insert(smsReview);
        if (insert > 0) {
            // 寻找关注点评人的粉丝用户
            List<UmsFollow> umsFollows = umsFollowMapper.selectList(new LambdaQueryWrapper<UmsFollow>().eq(UmsFollow::getFolloweeId, id));
            // 推送点评到所有关注的粉丝用户
            umsFollows.forEach(umsFollow -> {
                redisService.zAdd(reviewPush + ":" + umsFollow.getFollowerId(), smsReview.getId(), System.currentTimeMillis());
            });
        }
        return insert;
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
            smsReviewDetail.setIsLike(isLike(smsReviewDetail.getId()));
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
        // 获取当前点评前5的点赞信息
        Set<Long> likes = redisService.zRange(reviewLike + ":" + smsReview.getId(), 0, 4);
        if (CollUtil.isNotEmpty(likes)) {
            List<Long> likeMemberIds = likes.stream().collect(Collectors.toList());
            String join = StrUtil.join(",", likeMemberIds);
            List<UmsMember> likeMembers = umsMemberService.query()
                    .in("id", likeMemberIds)
                    .last("ORDER BY FIELD(id," + join + ")")
                    .list();
            smsReviewDetail.setLikeMembers(likeMembers);
        }
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
                redisService.zRemove(reviewLike + ":" + id, memberId, System.currentTimeMillis());
            }
            return flag;
        }
        // 点评的点赞数量 + 1
        flag = this.update().setSql("likes = likes + 1").eq("id", id).update();
        if (flag) {
            // 将用户的点赞信息持久化到redis中的ZSet结构中
            redisService.zAdd(reviewLike + ":" + id, memberId, System.currentTimeMillis());
        }
        return flag;
    }

    @Override
    public ScrollResult follow(Long max, Integer offSet) throws InterruptedException {
        // 从用户推送箱中获取推送博客信息
        Long id = MemberHolder.get().getId();
        if (max == null) {
            max = Long.MAX_VALUE;
        }
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(reviewPush + ":" + id, 0, max, offSet, 3);
        // 非空判断
        if (typedTuples == null || typedTuples.isEmpty()) {
            return null;
        }
        List<Long> reviewIds = new ArrayList<>(typedTuples.size());
        // 实现滚动分页查询
        Long maxTime = 0L;
        Integer os = 1;
        for (ZSetOperations.TypedTuple<Object> typedTuple : typedTuples) {
            reviewIds.add(Long.valueOf(typedTuple.getValue().toString()));
            // 获取score时时间戳
            long time = typedTuple.getScore().longValue();
            if (time == maxTime) {
                os++;
            } else {
                maxTime = time;
                os = 1;
            }
        }
        String join = StrUtil.join(",", reviewIds);
        // 查询推送的点评内容，保证顺序性
        List<SmsReview> smsReviews = smsReviewMapper.selectList(new LambdaQueryWrapper<SmsReview>()
                .in(SmsReview::getId, reviewIds)
                .last("ORDER BY FIELD(id," + join  +")"));
        // 获取点评的相关详细信息
        List<SmsReviewDetail> smsReviewDetails = new ArrayList<>(smsReviews.size());
        for (SmsReview smsReview : smsReviews) {
            SmsReviewDetail smsReviewDetail = new SmsReviewDetail();
            BeanUtil.copyProperties(smsReview, smsReviewDetail, false);
            // 获取商铺和用户信息
            SmsStore smsStore = smsStoreService.detail(smsReviewDetail.getStoreId());
            smsReviewDetail.setSmsStore(smsStore);
            UmsMember umsMember = umsMemberService.detail(smsReviewDetail.getMemberId());
            smsReviewDetail.setUmsMember(umsMember);
            // 获取当前用户的点评点赞状态
            smsReviewDetail.setIsLike(isLike(id));
            smsReviewDetails.add(smsReviewDetail);
        }
        return new ScrollResult()
                .setScroll(smsReviewDetails)
                .setMax(maxTime)
                .setOffset(os);
    }

    /**
     * 判断用户是否被点赞
     * @param id
     * @return
     */
    private Boolean isLike(Long id) {
        MemberDto memberDto = umsMemberService.info();
        if (memberDto != null) {
            // 从ThreadLocal中获取当前用户id
            Long memberId = umsMemberService.info().getId();
            // 判断该用户的点赞信息是否存在于Redis的ZSet结构中
            return redisService.zScore(reviewLike + ":" + id, memberId) != null;
        }
        return false;
    }
}
