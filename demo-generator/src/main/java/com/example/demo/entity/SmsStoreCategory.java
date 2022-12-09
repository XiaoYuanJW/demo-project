package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
import java.util.List;

/**
* 商铺分类表实体类 : sms_store_category
* Created by YuanJW on 2022-12-08 13:43:36
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value ="sms_store_category")
@ApiModel(value = "SmsStoreCategory", description = "商铺分类表")
public class SmsStoreCategory extends BaseEntity {
    @NotNull(message="[商铺分类id]不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "商铺分类id")
    private Long id;
    
    @NotNull(message="[商铺上级分类id（0表示一级分类）]不能为空")
    @ApiModelProperty(value = "商铺上级分类id（0表示一级分类）")
    private Long parentId;
    
    @NotBlank(message="[商铺名称]不能为空")
    @Size(max= 128,message="编码长度不能超过128")
    @Length(max= 128,message="编码长度不能超过128")
    @ApiModelProperty(value = "商铺名称")
    private String name;

    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "上级分类id列表")
    private String ancestor;
    
    @NotNull(message="[商铺分类级别：0级->1级->2级->3级]不能为空")
    @ApiModelProperty(value = "商铺分类级别：0级->1级->2级->3级")
    private Integer level;

    @Size(max= -1,message="编码长度不能超过-1")
    @Length(max= -1,message="编码长度不能超过-1")
    @ApiModelProperty(value = "简介描述")
    private String description;
    
    @Size(max= 512,message="编码长度不能超过512")
    @Length(max= 512,message="编码长度不能超过512")
    @ApiModelProperty(value = "商铺图片")
    private String image;
    
    @ApiModelProperty(value = "商铺数量")
    private Integer storeCount;
    
    @ApiModelProperty(value = "销量")
    private Integer sale;
    
    @NotNull(message="[商铺排名]不能为空")
    @ApiModelProperty(value = "商铺排名")
    private Integer sort;
    
    @NotNull(message="[显示状态：0->不显示 1->显示]不能为空")
    @ApiModelProperty(value = "显示状态：0->不显示 1->显示")
    private Integer showStatus;
    
    @NotNull(message="[导航栏显示状态：0->不显示；1->显示]不能为空")
    @ApiModelProperty(value = "导航栏显示状态：0->不显示；1->显示")
    private Integer navStatus;

    @TableField(exist = false)
    @ApiModelProperty(value = "商铺图片信息")
    private List<SysFile> imageInfo;
}
