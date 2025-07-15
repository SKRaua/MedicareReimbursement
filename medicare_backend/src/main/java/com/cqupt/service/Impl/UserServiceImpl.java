package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.dto.UserDTO;
import com.cqupt.mapper.MenuMapper;
import com.cqupt.mapper.RoleMenuMapper;
import com.cqupt.mapper.UserMapper;
import com.cqupt.pojo.Menu;
import com.cqupt.pojo.RoleMenu;
import com.cqupt.pojo.User;
import com.cqupt.service.UserService;
import com.cqupt.utils.ResultVo;
import com.cqupt.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zhou Xinyang
 * @date 2025/07/07
 * @description
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResultVo<UserDTO> login(String username, String password, Integer roleId) throws Exception {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", username);
        qw.eq("password", password);
        User user = getOne(qw);

        if (user != null) {
            // 核心验证：roleId是否一致
            if (!user.getRoleId().equals(roleId)) {
                return ResultVo.fail("身份认证错误，请重新登录");
            }

            // 根据用户角色获取当前用户的菜单
            QueryWrapper<RoleMenu> listRoleQw = new QueryWrapper<>();
            listRoleQw.eq("role_id", user.getRoleId());
            listRoleQw.select("menu");
            List<Integer> menuIds = roleMenuMapper.selectObjs(listRoleQw)
                    .stream()
                    .map(obj -> (Integer) obj)
                    .collect(Collectors.toList());

            // 根据menu_id查询一级菜单列表
            List<Menu> menus = menuMapper.selectBatchIds(menuIds);
            // 查询子菜单
            for (Menu menu : menus) {
                QueryWrapper<Menu> listMenuQw = new QueryWrapper<>();
                listMenuQw.eq("parent_id", menu.getId());
                menu.setChildren(menuMapper.selectList(listMenuQw));
            }

            // 构建返回对象
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setNickname(user.getNickname());
            userDTO.setRoleId(user.getRoleId());
            userDTO.setMenuList(menus);

            // 使用JwtUtil生成token（确保包含roleId）
            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRoleId());

            return ResultVo.ok(userDTO, token);
        }
        return ResultVo.fail("登录失败，检查账号密码");
    }

    @Override
    public ResultVo<Void> add(User user) throws Exception {
        user.setRoleId(1);
        userMapper.insert(user);
        return ResultVo.ok("添加成功");
    }

    @Override
    public ResultVo<Void> update(User user) throws Exception {
        userMapper.updateById(user);
        return ResultVo.ok("修改成功");
    }

    @Override
    public ResultVo<Void> remove(Integer id) throws Exception {
        if (id == null) {
            return ResultVo.fail("删除失败，用户ID不能为空");
        }
        userMapper.deleteById(id);
        return ResultVo.ok("删除成功");
    }

    @Override
    public ResultVo<Page<User>> listPage(UserDTO userDTO) throws Exception {
        // 分页对象
        Page<User> myPage = new Page<>(userDTO.getCurrentPage(), userDTO.getPageSize());
        // 查询条件
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("role_id", userDTO.getRoleId());

        // 模糊查询
        if (userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) {
            qw.like("username", userDTO.getUsername());
        }
        if (userDTO.getNickname() != null && !userDTO.getNickname().isEmpty()) {
            qw.like("nickname", userDTO.getNickname());
        }

        // 执行分页查询
        page(myPage, qw);
        return ResultVo.ok(myPage);
    }
}
