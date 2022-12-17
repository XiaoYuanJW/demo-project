package com.example.demo.controller;

import com.example.demo.api.CommonResult;
import com.example.demo.entity.SmsCoupon;
import com.example.demo.service.SmsCouponService;
import com.example.demo.validator.groups.insert;
import com.example.demo.validator.groups.update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 优惠券操作接口
 * Created by YuanJW on 2022/12/16.
 */
@RestController
@Api(tags = "SmsCouponController", description = "优惠券操作接口")
@RequestMapping("/coupon")
public class SmsCouponController {
    @Resource
    private SmsCouponService smsCouponService;

    @ApiOperation(value = "新增优惠券")
    @PostMapping(value = "/insert", produces = "application/json;charset=UTF-8")
    public CommonResult insert(@Validated(insert.class) @RequestBody SmsCoupon smsCoupon) {
        int count = smsCouponService.insert(smsCoupon);
        if (count > 0) {
            return CommonResult.success("新增成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "修改优惠券")
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public CommonResult update(@Validated(update.class) @RequestBody SmsCoupon smsCoupon) {
        int count = smsCouponService.update(smsCoupon);
        if (count > 0) {
            return CommonResult.success("新增成功");
        }
        return CommonResult.failed();
    }

}
