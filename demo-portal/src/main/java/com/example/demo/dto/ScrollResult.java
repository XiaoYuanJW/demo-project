package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 滚动结果
 * Created by YuanJW on 2023/1/3.
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "MemberDto", description = "滚动结果")
public class ScrollResult<T> {
    @ApiModelProperty("滚动结果列表")
    private List<T> scroll;
    @ApiModelProperty("最大值")
    private Long max;
    @ApiModelProperty("偏移量")
    private Integer offset;
}
