package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.cqupt.dto.UserDTO;
import com.cqupt.mapper.MenuMapper;
import com.cqupt.mapper.RoleMenuMapper;
import com.cqupt.mapper.UserMapper;
import com.cqupt.pojo.Menu;
import com.cqupt.pojo.RoleMenu;
import com.cqupt.pojo.User;
import com.cqupt.service.UserService;
import com.cqupt.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
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

    @Override
    public ResultVo<UserDTO> login(String username, String password,Integer roleId) throws Exception {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", username);
        qw.eq("password", password);
        User user = getOne(qw);
        if (user != null) {
            //核心验证：roleId是否一致
            if(!user.getRoleId().equals(roleId)){
                return ResultVo.fail("身份选择错误，请重新选择");
            }
            // 根据用户角色获取当前用户的菜单
            // 获取得到角色对应的menu_id
            QueryWrapper<RoleMenu> listRoleQw = new QueryWrapper<>();
            listRoleQw.eq("role_id", user.getRoleId());
            listRoleQw.select("menu");
            List<Integer> menuIds = roleMenuMapper.selectObjs(listRoleQw)
                    .stream()
                    .map(obj -> (Integer) obj)
                    .collect(Collectors.toList()); // 管理员 

            // 根据menu_id查询一级菜单列表
            List<Menu> menus = menuMapper.selectBatchIds(menuIds); // 
            // 查询子菜单
            for (Menu menu : menus) {
                QueryWrapper<Menu> listMenuQw = new QueryWrapper<>();
                listMenuQw.eq("parent_id", menu.getId()); // 1
                menu.setChildren(menuMapper.selectList(listMenuQw));
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setMenuList(menus);

            // 生成token
            HashMap<String, Object> map = new HashMap<>();
            userDTO.setId(user.getId());
            userDTO.setNickname(user.getNickname());
            userDTO.setUsername(user.getUsername());
            userDTO.setRoleId(user.getRoleId());
            // 如果登录验证成功,则需要生成令牌token（token就是按照特定规则生成的字符串）
            JwtBuilder builder = Jwts.builder();
            String token = builder.setSubject(username) // 主题，就是token中携带的数据
                    .setIssuedAt(new Date()) // 设置token的生成时间
                    .setId(userDTO.getId().toString()) // 设置用户id为token id
                    .setClaims(map) // map中可以存放用户的角色权限信息
                    .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 设置过期时间
                    .signWith(SignatureAlgorithm.HS256, "skraua2025") // 设置加密方式和加密密钥
                    .compact();
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
        if(id==null){
            return ResultVo.fail("删除失败，用户ID不能为空");
        }
        userMapper.deleteById(id);
        return ResultVo.ok("删除成功");
    }

    @Override
    public ResultVo<Page<User>> listPage(UserDTO userDTO) throws Exception {
        // 正确初始化分页对象：当前页码（默认1）、每页条数（默认10）
        int currentPage = userDTO.getCurrentPage() == null ? 1 : userDTO.getCurrentPage();
        int pageSize = userDTO.getPageSize() == null ? 10 : userDTO.getPageSize();
        Page<User> myPage = new Page<>(currentPage, pageSize); // 顺序：页码 → 每页条数

        QueryWrapper<User> qw = new QueryWrapper<>();
        // 角色固定为医疗操作员（覆盖前端传递的值，确保安全）
        qw.eq("role_id", 1);
        // 模糊查询（原代码用eq精确匹配，改为like实现搜索功能）
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
