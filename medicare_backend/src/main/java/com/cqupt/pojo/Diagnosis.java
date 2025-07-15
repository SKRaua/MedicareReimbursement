package com.cqupt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description 诊断记录实体类
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "Diagnosis", description = "诊断记录实体对象")
public class Diagnosis implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "患者ID")
    private Integer patientId;

    @ApiModelProperty(value = "疾病ID")
    private Integer diseaseId;

    @ApiModelProperty(value = "诊断类型（门诊/住院/急诊）")
    private String diagnosisType;

    @ApiModelProperty(value = "诊断日期")
    private LocalDate diagnosisDate;

    @ApiModelProperty(value = "诊断医生")
    private String diagnoseDoctor;

    @ApiModelProperty(value = "备注")
    private String remark;
}