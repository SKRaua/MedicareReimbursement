package com.cqupt.config;

import com.cqupt.interceptor.CheckTokenInterceptor;
import com.cqupt.utils.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Wang Yanchen
 * @date 2025/07/12/08:58:19
 * @description 全局Web配置
 * 
 *              权限控制说明：
 *              - 管理员 (roleId=0): 拥有所有权限，可以访问所有接口
 *              - 医院操作员 (roleId=1):
 *              * 所有查询接口
 *              * DiagnosisController 全部权限
 *              * DrugOrderController 全部权限
 *              * InsurederController 全部权限
 *              * MedicalServiceOrderController 全部权限
 *              * TreatmentItemOrderController 全部权限
 *              - 报销管理员 (roleId=2):
 *              * 所有查询接口
 *              * InsurederController 全部权限
 *              * ReimbursementRecordController 全部权限
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 全局配置，如拦截器、视图解析器等
    @Autowired
    private CheckTokenInterceptor checkTokenInterceptor;

    // 添加拦截器配置
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 解决跨域问题
        // WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(checkTokenInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/user/login", // 登录接口放行
                        "/captcha", // 验证码接口放行
                        "/logout", // 登出接口放行
                        "/static/**", // 静态资源放行
                        "/images/**", // 图片资源放行
                        "favicon.ico", // 网站图标放行
                        // 排除Swagger相关路径（关键）
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**");
    }

    // 全局跨域配置
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**") // 所有接口
                .allowedOriginPatterns("*") // 允许所有前端域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    // 解决日期格式问题
    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        // 添加自定义的日期格式化器
        registry.addConverter(new DateConverter());
    }
}

// 自定义视图解析器配置
