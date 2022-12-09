package com.example.demo.controller;

import com.example.demo.api.CommonResult;
import com.example.demo.service.SmsStoreCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 商铺种类信息管理Controller
 * Created by YuanJW on 2022/12/8.
 */
@RestController
@Api(tags = "SmsStoreCategoryController", description = "商铺种类信息管理")
@RequestMapping("/store/category")
public class SmsStoreCategoryController {
    @Resource
    private SmsStoreCategoryService smsStoreCategoryService;

    @ApiOperation(value = "获取商铺种类详情")
    @GetMapping(value = "/detail/{id}", produces = "application/json;charset=UTF-8")
    public CommonResult detail(@PathVariable Long id) {
        return CommonResult.success(smsStoreCategoryService.detail(id));
    }
}
