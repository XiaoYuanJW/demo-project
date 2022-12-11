package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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

/**
* 优惠券记录表实体类 : sms_coupon_history
* Created by YuanJW on 2022-12-09 15:33:39
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value ="sms_coupon_history")
@ApiModel(value = "SmsCouponHistory", description = "优惠券记录表")
public class SmsCouponHistory extends BaseEntity {
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message="[优惠券记录id]不能为空")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "优惠券记录id")
    private Long id;
    
    @NotNull(message="[会员id]不能为空")
    @ApiModelProperty(value = "会员id")
    private Long memberId;
    
    @NotNull(message="[优惠券id]不能为空")
    @ApiModelProperty(value = "优惠券id")
    private Long couponId;
    
    @NotBlank(message="[优惠券编码]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "优惠券编码")
    private String couponCode;
    
    @NotNull(message="[获取类型：0->后台赠送；1->主动获取 2->用户购买]不能为空")
    @ApiModelProperty(value = "获取类型：0->后台赠送；1->主动获取 2->用户购买")
    private Integer getType;
    
    @NotNull(message="[支付方式：0->无需支付 1->支付宝 2->微信支付]不能为空")
    @ApiModelProperty(value = "支付方式：0->无需支付 1->支付宝 2->微信支付")
    private Integer payType;
    
    @ApiModelProperty(value = "使用状态：0->未使用 1->已使用 2->已过期")
    private Integer useStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "使用时间")
    private Date useTime;
    
    @ApiModelProperty(value = "订单编号")
    private Long orderId;

    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "订单号码")
    private String orderSn;
}
