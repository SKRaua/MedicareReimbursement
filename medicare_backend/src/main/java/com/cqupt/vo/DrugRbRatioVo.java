package com.cqupt.vo;

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
 * @date 2025/07/11
 * @description DrugRbRatioVo
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "DrugRbRatioVo", description = "DrugRbRatioVo")
public class DrugRbRatioVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "药品类型（甲类/乙类/丙类）")
    private String drugType;

    @ApiModelProperty(value = "报销比例（0-1之间的小数）")
    private BigDecimal drugRatio;

    @ApiModelProperty(value = "状态（启用/停用）")
    private String status;

    @ApiModelProperty(value = "生效日期")
    private LocalDate effectiveDate;
}