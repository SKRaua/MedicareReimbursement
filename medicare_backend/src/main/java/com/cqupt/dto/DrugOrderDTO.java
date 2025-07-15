package com.cqupt.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Zhou Xinyang
 * @date 2025/07/09
 * @description DrugOrderDTO
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "DrugOrderDTO", description = "DrugOrderDTO")
public class DrugOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "患者ID")
    private Integer patientId; // 对应数据库字段：patient_id

    @ApiModelProperty(value = "药品ID（关联药品表）")
    private Integer drugId; // 对应数据库字段：drug_id

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开单时间")
    private LocalDateTime orderTime; // 对应数据库字段：order_time（datetime类型）

    @ApiModelProperty(value = "药品数量")
    private Integer quantity; // 对应数据库字段：quantity

    @ApiModelProperty(value = "开单医生")
    private String doctorName; // 对应数据库字段：doctor_name（varchar(50)）

    // Drug
    @ApiModelProperty(value = "医保类型")
    private String drugType; // 对应数据库字段：drug_type

    @ApiModelProperty(value = "页码")
    private Integer page;
}