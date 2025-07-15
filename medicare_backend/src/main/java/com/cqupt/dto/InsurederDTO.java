package com.cqupt.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Zhou Xinyang
 * @date 2025/07/08
 * @description 患者信息实体类
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "Insureder", description = "患者信息实体对象")
public class InsurederDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "身份证号")
    private String idCard; // 对应数据库字段：id_card

    @ApiModelProperty(value = "性别 0-女 1-男 2-未知")
    private Integer gender;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "出生日期")
    private LocalDate birthDate; // 对应数据库字段：birth_date（DATE类型）

    @ApiModelProperty(value = "家庭地址")
    private String address;

    @ApiModelProperty(value = "工作状态")
    private String workStatus; // 对应数据库字段：work_status

    @ApiModelProperty(value = "联系电话")
    private String contactPhone; // 对应数据库字段：contact_phone

    @ApiModelProperty(value = "住院号")
    private String inpatientNo;

    @ApiModelProperty(value = "结算类型")
    private String settlementType; // 对应数据库字段：settlement_type

    @ApiModelProperty(value = "入院时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime admissionTime; // 对应数据库字段：admission_time（DATETIME类型）

    @ApiModelProperty(value = "出院时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dischargeTime; // 对应数据库字段：discharge_time（DATETIME类型）

    @ApiModelProperty(value = "紧急联系人")
    private String emergencyContact; // 对应数据库字段：

    @ApiModelProperty(value = "页码")
    private Integer page;
}