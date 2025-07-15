package com.cqupt.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "ReimbursementRatioDTO", description = "报销比例实体对象DTO")
public class ReimbursementRatioDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "医院等级（一级/二级/三级）")
    private String hospitalLevel;

    @ApiModelProperty(value = "人员类型（在职/退休/特殊人群）")
    private String staffType;

    @ApiModelProperty(value = "起付金额")
    private BigDecimal startAmount;

    @ApiModelProperty(value = "封顶金额")
    private BigDecimal endAmount;

    @ApiModelProperty(value = "报销比例")
    private BigDecimal ratio;

    @ApiModelProperty(value = "状态（启用/停用）")
    private String status;

    @ApiModelProperty(value = "生效日期")
    private LocalDate effectiveDate;

    @ApiModelProperty(value = "页码")
    private Integer page;

    @ApiModelProperty(value="备注")
    private String remark;
}