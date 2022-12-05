package com.example.demo.entity;

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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
* 文件信息实体类 : sys_file
* Created by YuanJW on 2022-11-18 16:50:21
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value ="sys_file")
@ApiModel(value = "SysFile", description = "文件信息实体类")
public class SysFile extends BaseEntity {
    @NotNull(message="[文件id]不能为空")
    @ApiModelProperty(value = "文件id")
    private Long id;
    
    @NotNull(message="[文件存储位置：1->本地 2->minio]不能为空")
    @ApiModelProperty(value = "文件存储位置：1->本地 2->minio")
    private Integer location;

    @Size(max= 64,message="编码长度不能超过64")
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty(value = "文件桶")
    private String bucket;

    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "文件原始名称")
    private String originName;

    @Size(max= 32,message="编码长度不能超过32")
    @Length(max= 32,message="编码长度不能超过32")
    @ApiModelProperty(value = "文件后缀")
    private String suffix;
    
    @ApiModelProperty(value = "文件大小（单位：kb）")
    private BigDecimal size;

    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过255")
    @ApiModelProperty(value = "文件名称")
    private String objectName;

    @Size(max= 255,message="编码长度不能超过255")
    @Length(max= 255,message="编码长度不能超过512")
    @ApiModelProperty(value = "更新者")
    private String path;
}
