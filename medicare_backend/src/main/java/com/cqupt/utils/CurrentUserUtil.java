package com.cqupt.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class CurrentUserUtil {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取当前请求的token
     */
    private String getCurrentToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getHeader("token");
        }
        return null;
    }

    /**
     * 获取当前用户ID
     */
    public Integer getCurrentUserId() {
        String token = getCurrentToken();
        if (token != null) {
            return jwtUtil.getUserIdFromToken(token);
        }
        throw new RuntimeException("无法获取当前用户信息");
    }

    /**
     * 获取当前用户名
     */
    public String getCurrentUsername() {
        String token = getCurrentToken();
        if (token != null) {
            return jwtUtil.getUsernameFromToken(token);
        }
        throw new RuntimeException("无法获取当前用户信息");
    }

    /**
     * 获取当前用户角色ID
     */
    public Integer getCurrentUserRoleId() {
        String token = getCurrentToken();
        if (token != null) {
            return jwtUtil.getRoleIdFromToken(token);
        }
        throw new RuntimeException("无法获取当前用户角色信息");
    }

    /**
     * 判断当前用户是否为管理员
     */
    public boolean isAdmin() {
        return getCurrentUserRoleId() == 0;
    }

    /**
     * 判断当前用户是否为医院操作员
     */
    public boolean isHospitalOperator() {
        return getCurrentUserRoleId() == 1;
    }

    /**
     * 判断当前用户是否为报销管理员
     */
    public boolean isReimbursementAdmin() {
        return getCurrentUserRoleId() == 2;
    }
}