package com.example.demo.controller;

import com.example.demo.api.CommonPage;
import com.example.demo.api.CommonResult;
import com.example.demo.entity.UmsMemberSign;
import com.example.demo.service.UmsMemberSignService;
import com.example.demo.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 会员签到管理
 * Created by YuanJW on 2023/1/5.
 */
@RestController
@Api(tags = "UmsMemberSignController", description = "会员签到管理")
@RequestMapping("/sign")
public class UmsMemberSignController {
    @Resource
    private UmsMemberSignService umsMemberSignService;

    @ApiOperation(value = "查询签到活动")
    @GetMapping("page")
    public CommonResult<CommonPage<UmsMemberSign>> page(UmsMemberSign umsMemberSign) {
        PageUtils.startPage();
        return CommonResult.success(CommonPage.restPage(umsMemberSignService.page(umsMemberSign)));
    }

    @ApiOperation(value = "查询签到详情")
    @GetMapping("/detail/{id}")
    public CommonResult detail(@PathVariable Long id) {
        return CommonResult.success(umsMemberSignService.detail(id));
    }

    @ApiOperation(value = "会员签到")
    @PostMapping("/sign/{signId}")
    public CommonResult sign(@PathVariable Long signId) {
        boolean flag = umsMemberSignService.sign(signId);
        if (flag) {
            return CommonResult.success("签到成功");
        }
        return CommonResult.failed("签到失败");
    }

    @ApiOperation(value = "查看签到统计")
    @GetMapping("/statistics/{signId}")
    public CommonResult statistics(@PathVariable Long signId) {
        return CommonResult.success(umsMemberSignService.statistics(signId));
    }
}
