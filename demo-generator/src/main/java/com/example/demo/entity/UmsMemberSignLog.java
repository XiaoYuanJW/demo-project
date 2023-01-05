package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
* 会员签到日志表 : ums_member_sign_log
* Created by YuanJW on 2023-01-05 15:08:10
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value ="ums_member_sign_log")
@ApiModel(value = "UmsMemberSignLog", description = "会员签到日志表")
public class UmsMemberSignLog extends BaseEntity {
    @NotNull(message = "[商铺id]不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "商铺id")
    private Long id;
    
    @NotNull(message = "[用户id]不能为空")
    @ApiModelProperty(value = "用户id")
    private Long memberId;
    
    @NotNull(message = "[签到id]不能为空")
    @ApiModelProperty(value = "签到id")
    private Long signId;
    
    @NotNull(message = "[签到状态：0->未签 1->已签 2->补签]不能为空")
    @ApiModelProperty(value = "签到状态：0->未签 1->已签 2->补签")
    private Integer backupStatus;
    
    @NotNull(message = "[签到时间]不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "签到时间")
    private Date signTime;
}
