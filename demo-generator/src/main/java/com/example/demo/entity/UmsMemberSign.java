package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

/**
* 签到活动表 : ums_member_sign
* Created by YuanJW on 2023-01-05 15:08:10
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value ="ums_member_sign")
@ApiModel(value = "UmsMemberSign", description = "签到活动表")
public class UmsMemberSign extends BaseEntity {
    @NotNull(message = "[签到活动id]不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "签到活动id")
    private Long id;
    
    @NotBlank(message = "[签到活动名称]不能为空")
    @Size(max = 64, message = "编码长度不能超过64")
    @Length(max = 64, message = "编码长度不能超过64")
    @ApiModelProperty(value = "签到活动名称")
    private String name;
    
    @NotBlank(message = "[签到活动主题]不能为空")
    @Size(max = 255, message = "编码长度不能超过255")
    @Length(max = 255, message = "编码长度不能超过255")
    @ApiModelProperty(value = "签到活动主题")
    private String title;
    
    @NotNull(message = "[显示状态：0->不显示 1->显示]不能为空")
    @ApiModelProperty(value = "显示状态：0->不显示 1->显示")
    private Integer showStatus;
}
