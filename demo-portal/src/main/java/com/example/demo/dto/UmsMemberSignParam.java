package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 会员签到参数
 * Created by YuanJW on 2023/1/5.
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "UmsMemberSignParam", description = "会员签到参数")
public class UmsMemberSignParam {
}
