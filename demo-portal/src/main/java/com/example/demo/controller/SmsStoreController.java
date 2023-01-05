package com.example.demo.controller;

import com.example.demo.api.CommonPage;
import com.example.demo.api.CommonResult;
import com.example.demo.dto.SearchStoreParam;
import com.example.demo.entity.SmsStore;
import com.example.demo.service.SmsStoreService;
import com.example.demo.utils.PageUtils;
import com.example.demo.validator.groups.insert;
import com.example.demo.validator.groups.update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
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
    public CommonResult detail(@PathVariable Long id) throws InterruptedException {
        return CommonResult.success(smsStoreService.detail(id));
    }

    @ApiOperation(value = "更新商铺信息")
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public CommonResult update(@Validated(update.class) @RequestBody SmsStore smsStore) {
        int count = smsStoreService.update(smsStore);
        if (count > 0) {
            return CommonResult.success("更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    @ApiOperation(value = "新增商铺信息")
    @PostMapping(value = "/insert", produces = "application/json;charset=UTF-8")
    public CommonResult insert(@Validated(insert.class) @RequestBody SmsStore smsStore) {
        int count = smsStoreService.insert(smsStore);
        if (count > 0) {
            return CommonResult.success("新增成功");
        }
        return CommonResult.failed("新增失败");
    }
    
    @ApiOperation(value = "获取商铺列表")
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public CommonResult<CommonPage<SmsStore>> list(SmsStore smsStore) {
        PageUtils.startPage();
        return CommonResult.success(CommonPage.restPage(smsStoreService.list(smsStore)));
    }

    @ApiOperation(value = "查询附近店铺")
    @GetMapping(value = "/search", produces = "application/json;charset=UTF-8")
    public CommonResult search(@Validated SearchStoreParam searchStoreParam) {
        return CommonResult.success(smsStoreService.search(searchStoreParam));
    }

    @ApiOperation(value = "店铺地理预热")
    @GetMapping(value = "/loadStoreGeo", produces = "application/json;charset=UTF-8")
    public void loadStoreGeo() {
        smsStoreService.loadStoreGeo();
    }
}
