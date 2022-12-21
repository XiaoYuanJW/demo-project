package com.example.demo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.SmsStore;
import com.example.demo.entity.SysFile;
import com.example.demo.entity.UmsMember;
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
 * 商铺点评详情
 * Created by YuanJW on 2022/12/19.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SmsReviewDetail extends BaseEntity {
    @NotNull(message = "[商铺点评id]不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "商铺点评id")
    private Long id;

    @NotNull(message = "[商铺id]不能为空")
    @ApiModelProperty(value = "商铺id")
    private Long storeId;

    @NotNull(message = "[会员id]不能为空")
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @NotBlank(message = "[点评标题]不能为空")
    @Size(max = 64, message = "编码长度不能超过64")
    @Length(max = 64, message = "编码长度不能超过64")
    @ApiModelProperty(value = "点评标题")
    private String title;

    @NotBlank(message = "[点评提要]不能为空")
    @Size(max = 255, message = "编码长度不能超过255")
    @Length(max = 255, message = "编码长度不能超过255")
    @ApiModelProperty(value = "点评提要")
    private String synopsis;

    @NotBlank(message = "[点评内容]不能为空")
    @Size(max = -1, message = "编码长度不能超过-1")
    @Length(max = -1, message = "编码长度不能超过-1")
    @ApiModelProperty(value = "点评内容")
    private String context;

    @Size(max = 1024, message = "编码长度不能超过1024")
    @Length(max = 1024, message = "编码长度不能超过1024")
    @ApiModelProperty(value = "点评图片")
    private String pic;

    @TableField(exist = false)
    @ApiModelProperty(value = "点评图片信息")
    private List<SysFile> picInfo;

    @NotNull(message = "[点赞数]不能为空")
    @ApiModelProperty(value = "点赞数")
    private Integer likes;

    @NotNull(message = "[浏览数]不能为空")
    @ApiModelProperty(value = "浏览数")
    private Integer views;

    @NotNull(message = "[评论数]不能为空")
    @ApiModelProperty(value = "评论数")
    private Integer comments;

    @NotNull(message = "[举报数]不能为空")
    @ApiModelProperty(value = "举报数")
    private Integer reports;

    @NotNull(message = "[商铺点评排名]不能为空")
    @ApiModelProperty(value = "商铺点评排名")
    private Integer sort;

    @NotNull(message = "[商品推荐状态；0->不推荐；1->推荐]不能为空")
    @ApiModelProperty(value = "商品推荐状态；0->不推荐；1->推荐")
    private Integer recommandStatus;

    @NotNull(message = "[商品审核状态：0->未审核；1->审核通过]不能为空")
    @ApiModelProperty(value = "商品审核状态：0->未审核；1->审核通过")
    private Integer verifyStatus;

    @ApiModelProperty(value = "用户信息")
    private UmsMember umsMember;

    @ApiModelProperty(value = "商铺信息")
    private SmsStore smsStore;

    @ApiModelProperty(value = "当前用户的点赞状态")
    private Boolean isLike;
}
