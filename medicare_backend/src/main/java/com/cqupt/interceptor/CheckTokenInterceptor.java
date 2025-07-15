package com.cqupt.interceptor;

import com.cqupt.annotation.RequireRole;
import com.cqupt.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class CheckTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

        // 放行预检请求
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 放行静态资源
        if (request.getRequestURI().contains("/images")) {
            return true;
        }

        // 只处理Controller方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Class<?> clazz = handlerMethod.getBeanType();

        // 获取请求头中的token
        String token = request.getHeader("token");

        // 调试日志
        System.out.println("=== Token验证开始 ===");
        System.out.println("请求URL: " + request.getRequestURI());
        System.out.println("请求方法: " + method.getName());
        System.out.println(
                "Token: " + (token != null ? token.substring(0, Math.min(token.length(), 20)) + "..." : "null"));

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("token令牌不存在，请先登录");
        }

        try {
            // 验证token有效性
            if (!jwtUtil.validateToken(token)) {
                throw new RuntimeException("token令牌无效或已过期，请重新登录");
            }

            // 获取用户角色信息
            Integer userRoleId = jwtUtil.getRoleIdFromToken(token);

            System.out.println("从Token中获取的角色ID: " + userRoleId);

            if (userRoleId == null) {
                throw new RuntimeException("用户角色信息不存在");
            }

            // 进行权限验证
            checkPermission(method, clazz, userRoleId);

            System.out.println("=== Token验证通过 ===");
            return true;

        } catch (Exception e) {
            System.err.println("Token验证失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("认证失败: " + e.getMessage());
        }
    }

    /**
     * 权限验证逻辑
     */
    private void checkPermission(Method method, Class<?> clazz, Integer userRoleId) {
        System.out.println("开始权限检查，用户角色: " + userRoleId);

        // 管理员(roleId=0)拥有所有权限
        if (userRoleId == 0) {
            System.out.println("管理员权限，直接通过");
            return;
        }

        // 检查方法上的权限注解
        RequireRole methodAnnotation = method.getAnnotation(RequireRole.class);
        // 检查类上的权限注解
        RequireRole classAnnotation = clazz.getAnnotation(RequireRole.class);

        // 优先使用方法级别的注解，其次是类级别的注解
        RequireRole requireRole = methodAnnotation != null ? methodAnnotation : classAnnotation;

        System.out.println("权限注解: " + (requireRole != null ? Arrays.toString(requireRole.value()) : "无"));

        // 如果没有权限注解，默认只允许管理员访问
        if (requireRole == null) {
            throw new RuntimeException("权限不足，该操作仅限管理员");
        }

        // 检查用户角色是否在允许的角色列表中
        int[] requiredRoles = requireRole.value();
        if (requiredRoles.length > 0) {
            boolean hasPermission = Arrays.stream(requiredRoles).anyMatch(role -> role == userRoleId);
            if (!hasPermission) {
                throw new RuntimeException("权限不足，无法访问该资源");
            }
        }

        System.out.println("权限检查通过");
    }
}
