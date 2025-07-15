package com.cqupt.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.cqupt.pojo.Menu;
import java.io.Serializable;
import java.util.List;

/**
 * @author Zhou Xinyang
 * @date 2025/07/07
 * @description 用户
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "UserDTO", description = "")
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "真实姓名")
    private String nickname;

     @ApiModelProperty(value = "密码")
     private String password;

    @ApiModelProperty(value = "系统角色编号 0-管理员 1-医院 2-费用报销")
    private Integer roleId;

    // 一个角色有多个菜单
    @ApiModelProperty(value = "角色菜单")
    @TableField(exist = false)
    private List<Menu> menuList;

    @ApiModelProperty(value="当前页码")
    private Integer currentPage;

    @ApiModelProperty(value="页面显示数目")
    private Integer pageSize;
}
