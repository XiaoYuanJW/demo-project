package com.example.demo.controller;

import com.example.demo.api.CommonResult;
import com.example.demo.service.UmsFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 会员关注管理Controller
 * Created by YuanJW on 2023/1/2.
 */
@RestController
@Api(tags = "UmsFollowController", description = "会员关注管理Controller")
@RequestMapping("/follow")
public class UmsFollowController {
    @Resource
    private UmsFollowService umsFollowService;

    @ApiOperation(value = "会员关注")
    @PostMapping("/follow/{followee}")
    public CommonResult follow(@PathVariable Long followee) {
        int follow = umsFollowService.follow(followee);
        if (follow > 0) {
            return CommonResult.success("关注成功");
        }
        return CommonResult.failed("关注失败");
    }

    @ApiOperation(value = "会员取关")
    @PostMapping("/unfollow/{followee}")
    public CommonResult unfollow(@PathVariable Long followee) {
        int unfollow = umsFollowService.unfollow(followee);
        if (unfollow > 0) {
            return CommonResult.success("取关成功");
        }
        return CommonResult.failed("取关失败");
    }

    @ApiOperation(value = "查询会员是否关注")
    @GetMapping("/isFollow/{followee}")
    public CommonResult isFollow(@PathVariable Long followee) {
        return CommonResult.success(umsFollowService.isFollow(followee));
    }

    @ApiOperation(value = "查询与指定用户的共同关注")
    @GetMapping("/common/{id}")
    public CommonResult common(@PathVariable Long id) {
        return CommonResult.success(umsFollowService.common(id));
    }
}
