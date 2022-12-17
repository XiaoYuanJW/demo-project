package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.validator.groups.insert;
import com.example.demo.validator.groups.update;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.math.BigDecimal;
import java.util.Date;

/**
* 优惠券表实体类 : sms_coupon
* Created by YuanJW on 2022-12-09 17:58:41
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value ="sms_coupon")
@ApiModel(value = "SmsCoupon", description = "优惠券表")
public class SmsCoupon extends BaseEntity {
    @NotNull(message="[优惠券id]不能为空", groups = {update.class})
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "优惠券id")
    private Long id;
    
    @NotNull(message="[商铺id]不能为空", groups = {insert.class})
    @ApiModelProperty(value = "商铺id")
    private Long storeId;
    
    @NotNull(message="[优惠卷类型；0->全场赠券 1->商铺赠券 2->会员赠券 3->用户购买 4->用户限时抢购]不能为空", groups = {insert.class})
    @ApiModelProperty(value = "优惠卷类型；0->全场赠券 1->商铺赠券 2->会员赠券 3->用户购买 4->用户限时抢购")
    private Integer type;
    
    @NotBlank(message="[商铺优惠券标题]不能为空", groups = {insert.class})
    @Size(max= 64,message="编码长度不能超过64")
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty(value = "商铺优惠券标题")
    private String title;
    
    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "商铺优惠券副标题")
    private String subTitle;
    
    @Size(max= -1,message="编码长度不能超过-1")
    @Length(max= -1,message="编码长度不能超过-1")
    @ApiModelProperty(value = "商铺优惠券详细说明")
    private String description;
    
    @NotNull(message="[优惠券价格]不能为空", groups = {insert.class})
    @ApiModelProperty(value = "优惠券价格")
    private BigDecimal price;
    
    @NotNull(message="[商铺优惠券数量]不能为空", groups = {insert.class})
    @ApiModelProperty(value = "商铺优惠券数量")
    private Integer count;
    
    @NotNull(message="[商铺优惠券金额]不能为空", groups = {insert.class})
    @ApiModelProperty(value = "商铺优惠券金额")
    private BigDecimal amount;
    
    @NotNull(message="[每人限领次数]不能为空", groups = {insert.class})
    @ApiModelProperty(value = "每人限领次数")
    private Integer perLimit;
    
    @ApiModelProperty(value = "使用门槛；0表示无门槛")
    private BigDecimal minPoint;
    
    @NotNull(message="[优惠券类型：0->全场通用 1->指定商铺 2->指定分类 3->指定商品]不能为空", groups = {insert.class})
    @ApiModelProperty(value = "优惠券类型：0->全场通用 1->指定商铺 2->指定分类 3->指定商品")
    private Integer useType;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "使用开始时间")
    private Date useStartTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "使用结束时间")
    private Date useEndTime;

    @ApiModelProperty(value = "领取数量")
    private Integer receiveCount;

    @ApiModelProperty(value = "剩余数量")
    private Integer surplusCount;

    @ApiModelProperty(value = "使用数量")
    private Integer useCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "领取开始时间")
    private Date receiveEndTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "领取结束时间")
    private Date receiveStartTime;

    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "优惠码")
    private String code;

    @ApiModelProperty(value = "优惠券状态：0->未开始 1->进行中 2->已结束")
    private Integer status;
}
