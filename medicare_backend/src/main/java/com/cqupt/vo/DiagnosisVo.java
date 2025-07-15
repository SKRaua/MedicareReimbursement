package com.cqupt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
@ApiModel(value = "DiagnosisVo", description = "诊断分页VO")
public class DiagnosisVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "患者ID")
    private Integer patientId;

    @ApiModelProperty(value = "疾病ID")
    private Integer diseaseId;

    @ApiModelProperty(value = "诊断类型")
    private String diagnosisType;

    @ApiModelProperty(value = "诊断日期")
    private LocalDate diagnosisDate;

    @ApiModelProperty(value = "诊断医生")
    private String diagnoseDoctor;

    @ApiModelProperty(value = "诊断说明")
    private String remark;

    // Disease
    @ApiModelProperty(value = "疾病名称")
    private String diseaseName;
}