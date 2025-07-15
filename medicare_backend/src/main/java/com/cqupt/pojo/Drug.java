package com.cqupt.pojo;

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
 * @date 2025/07/09
 * @description 药品信息实体类
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "Drug", description = "药品信息实体对象")
public class Drug implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "通用名称")
    private String drugName; // 对应数据库字段：drug_name

    @ApiModelProperty(value = "商品名称")
    private String tradeName; // 对应数据库字段：trade_name

    @ApiModelProperty(value = "规格（50mg/片）")
    private String specification; // 对应数据库字段：specification

    @ApiModelProperty(value = "计价单位（片/盒等）")
    private String unit; // 对应数据库字段：unit

    @ApiModelProperty(value = "生产企业")
    private String manufacturer; // 对应数据库字段：manufacturer

    @ApiModelProperty(value = "单价")
    private BigDecimal price; // 对应数据库字段：price（DECIMAL类型）

    @ApiModelProperty(value = "医保类型")
    private String drugType; // 对应数据库字段：drug_type

    @ApiModelProperty(value = "适应症/禁忌症等")
    private String remark; // 对应数据库字段：remark（TEXT类型）
}