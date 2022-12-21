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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
* 商铺点评评论表 : sms_review_comment
* Created by YuanJW on 2022-12-19 14:50:57
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value ="sms_review_comment")
@ApiModel(value = "SmsReviewComment", description = "商铺点评评论表")
public class SmsReviewComment extends BaseEntity {
    @NotNull(message = "[商铺点评评论id]不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "商铺点评评论id")
    private Long id;
    
    @NotNull(message = "[商铺点评id]不能为空")
    @ApiModelProperty(value = "商铺点评id")
    private Long reviewId;
    
    @NotNull(message = "[商铺点评评论用户id]不能为空")
    @ApiModelProperty(value = "商铺点评评论用户id")
    private Long memberId;
    
    @NotBlank(message = "[商铺点评评论内容]不能为空")
    @Size(max = -1, message = "编码长度不能超过-1")
    @Length(max = -1, message = "编码长度不能超过-1")
    @ApiModelProperty(value = "商铺点评评论内容")
    private String context;

    @Size(max = 1024, message = "编码长度不能超过1024")
    @Length(max = 1024, message = "编码长度不能超过1024")
    @ApiModelProperty(value = "商铺点评评论图片")
    private String pic;
    
    @ApiModelProperty(value = "商铺点评评论状态：0->禁用 1->显示")
    private Integer showStatus;
    
    @ApiModelProperty(value = "商铺点评评论回复数量")
    private Integer replyCount;
    
    @ApiModelProperty(value = "商铺点评评论等级：0-10")
    private Integer level;
}
