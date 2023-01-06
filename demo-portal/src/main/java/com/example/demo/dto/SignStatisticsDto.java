package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 签到统计返回结果
 * Created by YuanJW on 2023/1/5.
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SignStatisticsDto", description = "签到统计返回结果")
public class SignStatisticsDto {
    @ApiModelProperty(value = "连续签到天数")
    private Integer count;
    @ApiModelProperty(value = "签到记录")
    private String result;
}
