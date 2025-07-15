package com.cqupt.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Zhou Xinyang
 * @date 2025/07/10
 * @description DiseaseDTO
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "DiseaseDTO", description = "DiseaseDTO")
public class DiseaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "疾病名称")
    private String diseaseName;

    @ApiModelProperty(value = "疾病编码")
    private String diseaseCode;

    @ApiModelProperty(value = "ICD编码")
    private String icdCode;

    @ApiModelProperty(value = "疾病类型")
    private String diseaseType;

    @ApiModelProperty(value = "页码")
    private Integer page;

    @ApiModelProperty(value="每一页显示的条数")
    private Integer pageSize;

    @ApiModelProperty(value="备注")
    private String remark;
}