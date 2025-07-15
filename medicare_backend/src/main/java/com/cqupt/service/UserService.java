package com.cqupt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.dto.UserDTO;
import com.cqupt.pojo.User;
import com.cqupt.utils.ResultVo;

/**
 * @author Zhou Xinyang
 * @date 2025/07/01
 * @description
 */
public interface UserService extends IService<User> {
    public ResultVo<UserDTO> login(String username, String password,Integer roleId) throws Exception;

    public ResultVo<Void> add(User user) throws Exception;

    public ResultVo<Void> update(User user) throws Exception;

    public ResultVo<Void> remove(Integer id) throws Exception;

    public ResultVo<Page<User>>listPage(UserDTO userDTO) throws Exception;
}
