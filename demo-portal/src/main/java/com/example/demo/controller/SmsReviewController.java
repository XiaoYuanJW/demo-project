package com.example.demo.controller;

import com.example.demo.api.CommonPage;
import com.example.demo.api.CommonResult;
import com.example.demo.dto.SmsReviewDetail;
import com.example.demo.entity.SmsReview;
import com.example.demo.service.SmsReviewService;
import com.example.demo.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商铺点评接口
 * Created by YuanJW on 2022/12/19.
 */
@RestController
@Api(tags = "SmsReviewController", description = "商铺点评接口")
@RequestMapping("/review")
public class SmsReviewController {
    @Resource
    private SmsReviewService smsReviewService;

    @ApiOperation(value = "新增商铺点评")
    @PostMapping(value = "/insert", produces = "application/json;charset=UTF-8")
    public CommonResult insert(@RequestBody SmsReview smsReview) {
        int count = smsReviewService.insert(smsReview);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "分页查询商铺点评列表")
    @GetMapping(value = "/page", produces = "application/json;charset=UTF-8")
    public CommonResult<CommonPage<SmsReviewDetail>> page(SmsReview smsReview) {
        PageUtils.startPage();
        List<SmsReviewDetail> smsReviewDetailList = smsReviewService.page(smsReview);
        return CommonResult.success(CommonPage.restPage(smsReviewDetailList));
    }


    @ApiOperation(value = "查看商铺点评详情")
    @GetMapping(value = "/detail/{id}", produces = "application/json;charset=UTF-8")
    public CommonResult detail(@PathVariable Long id) throws InterruptedException {
        SmsReviewDetail smsReviewDetail = smsReviewService.detail(id);
        if (smsReviewDetail != null) {
            return CommonResult.success(smsReviewDetail);
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "点赞商铺点评")
    @PostMapping(value = "/like/{id}", produces = "application/json;charset=UTF-8")
    public CommonResult like(@PathVariable Long id) {
        boolean like = smsReviewService.like(id);
        if (like) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed();
    }
}
