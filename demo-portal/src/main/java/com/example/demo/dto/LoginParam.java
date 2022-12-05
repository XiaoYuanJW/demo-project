package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 登录参数
 * Created by YuanJW on 2022/12/4.
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "LoginParam", description = "登录参数")
public class LoginParam {
    @NotNull(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "验证码")
    private String authCode;
}
