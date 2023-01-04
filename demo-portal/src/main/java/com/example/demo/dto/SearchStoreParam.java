package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 搜索附近店铺参数
 * Created by YuanJW on 2023/1/4.
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SearchStoreParam", description = "搜索附近店铺参数")
public class SearchStoreParam {
    @NotNull(message = "[商铺种类]不能为空")
    @ApiModelProperty(value = "商铺种类")
    private Long categoryId;
    @ApiModelProperty(value = "地理坐标：经度")
    private BigDecimal lat;
    @ApiModelProperty(value = "地理坐标：维度")
    private BigDecimal lng;
    @ApiModelProperty(value = "距离（单位：米）")
    private Double distance;
}
