package com.example.demo.controller;

import com.example.demo.api.CommonResult;
import com.example.demo.dto.LoginParam;
import com.example.demo.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

/**
 * 会员信息管理Controller
 * Created by YuanJW on 2022/12/4.
 */
@RestController
@Api(tags = "UmsMemberController", description = "会员信息管理")
@RequestMapping("/member")
public class UmsMemberController {
    @Resource
    private UmsMemberService umsMemberService;

    @ApiOperation(value = "获取验证码")
    @GetMapping(value = "/getAuthCode", produces = "application/json;charset=UTF-8")
    public CommonResult getAuthCode(@RequestParam String phone,
                                    HttpSession httpSession) {
        return CommonResult.success(umsMemberService.getAuthCode(phone, httpSession), "获取验证码成功");
    }

    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public CommonResult login(@Valid @RequestBody LoginParam loginParam,
                              HttpSession httpSession){
        String token = umsMemberService.login(loginParam, httpSession);
        return CommonResult.success(token, "登录成功");
    }

    @ApiOperation(value = "获取会员信息")
    @GetMapping(value = "/info", produces = "application/json;charset=UTF-8")
    public CommonResult info() {
        return CommonResult.success(umsMemberService.info());
    }


    @ApiOperation(value = "获取会员信息详情")
    @GetMapping(value = "/detail/{id}", produces = "application/json;charset=UTF-8")
    public CommonResult detail(Long id) {
        return CommonResult.success(umsMemberService.detail(id));
    }

    @ApiOperation(value = "会员UV统计")
    @GetMapping(value = "/uv")
    public CommonResult uv(@RequestParam(value = "date", required = false)
                               @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
        return CommonResult.success(umsMemberService.uv(date));
    }
}
