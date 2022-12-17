package com.example.demo.controller;

import com.example.demo.api.CommonResult;
import com.example.demo.service.SmsCouponHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 优惠券记录接口
 * Created by YuanJW on 2022/12/9.
 */
@RestController
@Api(tags = "SmsCouponHistoryController", description = "优惠券记录信息管理")
@RequestMapping("/coupon/history")
public class SmsCouponHistoryController {
    @Resource
    private SmsCouponHistoryService smsCouponHistoryService;

    @ApiOperation(value = "新增优惠券记录")
    @PostMapping(value = "/purchase/{id}", produces = "application/json;charset=UTF-8")
    public CommonResult detail(@PathVariable Long id) {
        long historyId = smsCouponHistoryService.asyncPurchase(id);
        if (historyId > 0) {
            return CommonResult.success(historyId);
        }
        return CommonResult.failed();
    }
}
