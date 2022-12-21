package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.validator.groups.update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
* 用户表实体类 : ums_member
* Created by YuanJW on 2022-12-02 15:52:06
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value ="ums_member")
@ApiModel(value = "UmsMember", description = "用户表")
public class UmsMember extends BaseEntity {
    @NotNull(message="[用户id]不能为空", groups = {update.class})
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "用户id")
    private Long id;
    
    @NotBlank(message="[用户名]不能为空")
    @Size(max= 64,message="编码长度不能超过64")
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty(value = "用户名")
    private String name;
    
    @NotBlank(message="[电话]不能为空")
    @Size(max= 20,message="编码长度不能超过20")
    @Length(max= 20,message="编码长度不能超过20")
    @ApiModelProperty(value = "电话")
    private String phone;
    
    @NotBlank(message="[密码]不能为空")
    @Size(max= 100,message="编码长度不能超过100")
    @Length(max= 100,message="编码长度不能超过100")
    @TableField(value = "password", select = false)
    @ApiModelProperty(value = "密码")
    private String password;
    
    @NotNull(message="[性别：0->女 1->男]不能为空")
    @ApiModelProperty(value = "性别：0->女 1->男")
    private Integer gender;
    
    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    @Size(max= 64,message="编码长度不能超过64")
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty(value = "昵称")
    private String nickname;
    
    @ApiModelProperty(value = "地区id")
    private Long areaId;

    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "详细地址")
    private String detailArea;

    @Size(max= 512,message="编码长度不能超过512")
    @Length(max= 512,message="编码长度不能超过512")
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "邮箱")
    private String email;
    
    @ApiModelProperty(value = "粉丝数量")
    private Integer followeeNumber;
    
    @ApiModelProperty(value = "关注数量")
    private Integer followerNumber;

    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "个性签名")
    private String sign;
    
    @NotNull(message="[帐号启用状态：0->禁用；1->启用]不能为空")
    @ApiModelProperty(value = "帐号启用状态：0->禁用；1->启用")
    private Integer status;
    
    @NotNull(message="[积分]不能为空")
    @ApiModelProperty(value = "积分")
    private Integer integration;
    
    @NotNull(message="[历史积分值]不能为空")
    @ApiModelProperty(value = "历史积分值")
    private Integer historyIntegration;

    @TableField(exist = false)
    @ApiModelProperty(value = "头像信息")
    private List<SysFile> avatarInfo;
}
