package com.example.demo.controller;

import com.example.demo.api.CommonResult;
import com.example.demo.service.SmsStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 商铺管理Controller
 * Created by YuanJW on 2022/12/6.
 */
@RestController
@Api(tags = "SmsStoreController", description = "商铺信息管理")
@RequestMapping("/store")
public class SmsStoreController {
    @Resource
    private SmsStoreService smsStoreService;

    @ApiOperation(value = "获取商铺详情")
    @GetMapping(value = "/detail/{id}", produces = "application/json;charset=UTF-8")
    public CommonResult detail(@PathVariable Long id) {
        return CommonResult.success(smsStoreService.detail(id));
    }
}
