package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
* 商铺表实体类 : sms_store
* Created by YuanJW on 2022-12-06 22:08:52
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value ="sms_store")
@ApiModel(value = "SmsStore", description = "商铺表")
public class SmsStore extends BaseEntity{
    @NotNull(message="[商铺id]不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "商铺id")
    private Long id;
    
    @NotBlank(message="[商铺名称]不能为空")
    @Size(max= 128,message="编码长度不能超过128")
    @Length(max= 128,message="编码长度不能超过128")
    @ApiModelProperty(value = "商铺名称")
    private String name;
    
    @NotBlank(message="[商铺电话]不能为空")
    @Size(max= 20,message="编码长度不能超过20")
    @Length(max= 20,message="编码长度不能超过20")
    @ApiModelProperty(value = "商铺电话")
    private String phone;
    
    @NotNull(message="[商品类型：0->实体店 1->网店]不能为空")
    @ApiModelProperty(value = "商品类型：0->实体店 1->网店")
    private Integer type;
    
    @ApiModelProperty(value = "商铺种类id")
    private Long categoryId;
    
    @Size(max= -1,message="编码长度不能超过-1")
    @Length(max= -1,message="编码长度不能超过-1")
    @ApiModelProperty(value = "简介")
    private String introduce;
    
    @ApiModelProperty(value = "销量")
    private Integer sale;
    
    @Size(max= 512,message="编码长度不能超过512")
    @Length(max= 512,message="编码长度不能超过512")
    @ApiModelProperty(value = "商铺图片")
    private String image;

    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "详细地址")
    private String detailArea;
    
    @ApiModelProperty(value = "地理坐标：经度")
    private BigDecimal lat;
    
    @ApiModelProperty(value = "地理坐标：维度")
    private BigDecimal lng;
    
    @NotNull(message="[商铺排名]不能为空")
    @ApiModelProperty(value = "商铺排名")
    private Integer sort;
    
    @NotNull(message="[显示状态：0->不显示 1->显示]不能为空")
    @ApiModelProperty(value = "显示状态：0->不显示 1->显示")
    private Integer showStatus;
    
    @NotNull(message="[启用状态：0->关闭 1->启用]不能为空")
    @ApiModelProperty(value = "启用状态：0->关闭 1->启用")
    private Integer enableStatus;
    
    @NotNull(message="[商品推荐状态；0->不推荐；1->推荐]不能为空")
    @ApiModelProperty(value = "商品推荐状态；0->不推荐；1->推荐")
    private Integer recommandStatus;
    
    @NotNull(message="[商品审核状态：0->未审核；1->审核通过]不能为空")
    @ApiModelProperty(value = "商品审核状态：0->未审核；1->审核通过")
    private Integer verifyStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "每日营业开始时间")
    private Date businessStartTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "每日营业结束时间")
    private Date businessEndTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
