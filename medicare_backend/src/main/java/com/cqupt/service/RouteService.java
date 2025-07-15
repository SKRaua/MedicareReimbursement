package com.cqupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.pojo.Menu;

import java.util.List;

/**
 * @author Zhou Xinyang
 * @date 2025/07/08
 * @description
 */
public interface RouteService extends IService<Menu> {

    public List<Menu> getRoutesByRoleIds(List<Integer> roleIds) throws Exception;
}
