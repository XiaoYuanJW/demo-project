package com.example.demo.dto;

import com.example.demo.desensitization.Sensitive;
import com.example.demo.desensitization.SensitiveStrategy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Map;

/**
 * 用户返回信息
 * Created by YuanJW on 2022/12/5.
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "MemberDto", description = "用户返回信息")
public class MemberDto {
    @ApiModelProperty(value = "用户名")
    private String name;

    @Sensitive(value = SensitiveStrategy.MOBILE_PHONE)
    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "性别：0->女 1->男")
    private Integer gender;

    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "地区id")
    private Long areaId;

    @ApiModelProperty(value = "详细地址")
    private String detailArea;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "粉丝数量")
    private Integer followeeNumber;

    @ApiModelProperty(value = "关注数量")
    private Integer followerNumber;

    @ApiModelProperty(value = "个性签名")
    private String sign;

    @ApiModelProperty(value = "积分")
    private Integer integration;

    @ApiModelProperty(value = "历史积分值")
    private Integer historyIntegration;

    @ApiModelProperty(hidden = true)
    private Map<String, Object> files;
}
