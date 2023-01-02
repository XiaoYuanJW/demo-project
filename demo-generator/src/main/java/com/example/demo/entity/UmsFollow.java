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

import javax.validation.constraints.NotNull;

/**
* 用户关注 : ums_follow
* Created by YuanJW on 2023-01-02 16:26:33
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value ="ums_follow")
@ApiModel(value = "UmsFollow", description = "会员关注表")
public class UmsFollow extends BaseEntity {
    @NotNull(message = "[商铺id]不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "商铺id")
    private Long id;
    
    @NotNull(message = "[关注人id]不能为空")
    @ApiModelProperty(value = "关注人id")
    private Long followeeId;
    
    @NotNull(message = "[被关注人id]不能为空")
    @ApiModelProperty(value = "被关注人id")
    private Long followerId;
}
