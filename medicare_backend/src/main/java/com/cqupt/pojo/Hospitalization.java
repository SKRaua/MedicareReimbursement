package com.cqupt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Zhou Xinyang
 * @date 2025/07/08
 * @description 住院信息
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "Hospitalization对象", description = "住院信息实体对象")
public class Hospitalization implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "参保人编号")
    private Integer insurederId;

    @ApiModelProperty(value = "住院号")
    private String inpatientNo;

    @ApiModelProperty(value = "入院时间")
    private Date admissionTime;

    @ApiModelProperty(value = "出院时间")
    private Date dischargeTime;

    @ApiModelProperty(value = "紧急联系人")
    private String emergencyContact;

    @ApiModelProperty(value = "结算类型")
    private String settlementType;

}