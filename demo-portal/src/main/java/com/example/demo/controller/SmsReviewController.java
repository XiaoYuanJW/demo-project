package com.example.demo.controller;

import com.example.demo.api.CommonResult;
import com.example.demo.dto.SmsReviewDetail;
import com.example.demo.entity.SmsReview;
import com.example.demo.service.SmsReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    @ApiOperation(value = "查看商铺点评详情")
    @GetMapping(value = "/detail/{id}", produces = "application/json;charset=UTF-8")
    public CommonResult detail(@PathVariable Long id)  throws InterruptedException  {
        SmsReviewDetail smsReviewDetail = smsReviewService.detail(id);
        if (smsReviewDetail != null) {
            return CommonResult.success(smsReviewDetail);
        }
        return CommonResult.failed();
    }
}
