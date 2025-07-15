package com.cqupt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Zhou Xinyang
 * @date 2025/07/10
 * @description 药品分页VO
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "DrugVo", description = "药品分页VO")
public class DrugVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "通用名称")
    private String drugName;

    @ApiModelProperty(value = "商品名称")
    private String tradeName;

    @ApiModelProperty(value = "规格")
    private String specification;

    @ApiModelProperty(value = "计价单位")
    private String unit;

    @ApiModelProperty(value = "生产企业")
    private String manufacturer;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "医保类型")
    private String drugType;

    @ApiModelProperty(value = "适应症/禁忌症等")
    private String remark;
}