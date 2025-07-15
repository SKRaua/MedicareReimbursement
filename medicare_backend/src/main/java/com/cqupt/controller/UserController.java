package com.cqupt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.cqupt.dto.UserDTO;
import com.cqupt.service.UserService;
import com.cqupt.utils.ResultVo;
import com.cqupt.utils.CaptchaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Zhou Xinyang
 * @date 2025/07/01
 * @description
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaManager captchaManager;

    @ApiOperation("用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "username", value = "用户登录账号", required = true),
            @ApiImplicitParam(dataType = "string", name = "password", value = "用户登录密码", required = true),
            @ApiImplicitParam(dataType = "string", name = "captcha", value = "验证码", required = true),
            @ApiImplicitParam(dataType = "string", name = "captchaId", value = "验证码ID", required = true),
            @ApiImplicitParam(dataType = "int", name = "roleId", value = "用户角色ID", required = true)
    })
    @GetMapping("/login")
    public ResultVo<UserDTO> login(String username, String password, String captcha, String captchaId, Integer roleId)
            throws Exception {
        // 验证参数
        if (captchaId == null || captcha == null) {
            return ResultVo.fail("请输入验证码");
        }

        // 验证验证码（验证成功后自动移除）
        if (!captchaManager.validateAndRemove(captchaId, captcha)) {
            return ResultVo.fail("验证码错误或已过期，请重新输入");
        }

        // 调用原有的登录逻辑
        return userService.login(username, password, roleId);
    }

    // 用户管理操作只有管理员可以执行（没有权限注解，默认只有管理员可访问）
    @ApiOperation("添加用户")
    @PostMapping("/add")
    public ResultVo<Void> add(@RequestBody User user) throws Exception {
        return userService.add(user);
    }

    @ApiOperation("修改用户")
    @PostMapping("/update")
    public ResultVo<Void> update(@RequestBody User user) throws Exception {
        return userService.update(user);
    }

    @ApiOperation("删除用户")
    @PostMapping("/remove")
    public ResultVo<Void> remove(@RequestBody Map<String, Object> params) throws Exception {
        Integer id = (Integer) params.get("id");
        if (id == null) {
            return ResultVo.fail("id不能为空");
        }
        return userService.remove(id);
    }

    @ApiOperation("查询医疗操作人员列表（分页）")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "string", name = "username", value = "账号（模糊查询）", required = false),
            @ApiImplicitParam(dataType = "string", name = "nickname", value = "真实姓名（模糊查询）", required = false),
            @ApiImplicitParam(dataType = "int", name = "roleId", value = "角色ID（固定为1）", required = true),
    })
    @GetMapping("/listPage") // 查询用户列表也只有管理员可以访问
    public ResultVo<Page<User>> listPage(UserDTO userDTO) throws Exception {
        userDTO.setRoleId(1);
        // 调用Service层方法查询分页数据
        return userService.listPage(userDTO);
    }
}
