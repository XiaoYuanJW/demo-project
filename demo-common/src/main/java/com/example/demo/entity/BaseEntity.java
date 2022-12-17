package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用实体类
 * Created by YuanJW on 2022/12/2.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "备注")
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @Size(max= 64,message="编码长度不能超过64")
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty(value = "创建人")
    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;

    @Size(max= 64,message="编码长度不能超过64")
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty(value = "更新人")
    private String modifier;

    @ApiModelProperty(value = "注销标识字段： 0->正常；null->已注销")
    @TableLogic(value = "0",delval = "1")
    private Integer isDelete;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String searchValue;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private Map<String, Object> files;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public BaseEntity setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }
}
