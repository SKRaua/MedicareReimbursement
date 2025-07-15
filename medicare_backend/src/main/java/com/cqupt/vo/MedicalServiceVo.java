package com.cqupt.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Zhou Xinyang
 * @date 2025/07/10
 * @description MedicalServiceVo
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "MedicalServiceVo", description = "MedicalServiceVo")
public class MedicalServiceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目名称")
    private String itemName; // 对应数据库字段：item_name

    @ApiModelProperty(value = "项目编码")
    private String itemCode; // 对应数据库字段：item_code

    @ApiModelProperty(value = "国家统一编码")
    private String nationalCode; // 对应数据库字段：national_code

    @ApiModelProperty(value = "项目详情说明")
    private String description; // 对应数据库字段：description（TEXT类型）

    @ApiModelProperty(value = "除外内容")
    private String exclusion; // 对应数据库字段：exclusion（TEXT类型）

    @ApiModelProperty(value = "计价单位（次/小时等）")
    private String unit; // 对应数据库字段：unit

    @ApiModelProperty(value = "单价")
    private BigDecimal price; // 对应数据库字段：price（DECIMAL类型）

    @ApiModelProperty(value = "备注")
    private String remark; // 对应数据库字段：remark（TEXT类型）

    @ApiModelProperty(value = "财务分类")
    private String itemType; // 对应数据库字段：item_type
}