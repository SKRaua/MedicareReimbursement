package com.cqupt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Zhou Xinyang
 * @date 2025/07/07
 * @description Rolemenu实体对象
 */
@Data
@EqualsAndHashCode
//@TableName("Rolemenu") // 显式指定表名
@ApiModel(value = "RoleMenu", description = "RoleMenu实体对象")
public class RoleMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色编号")
    private Integer roleId;

    @ApiModelProperty(value = "菜单编号")
    private Integer menu;
}
