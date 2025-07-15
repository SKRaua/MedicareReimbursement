package com.cqupt.interceptor;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * @author Wang Yanchen
 * @date 2025/07/12/08:59:58
 * @description
 */
@Component
public class CheckTokenInterceptor implements HandlerInterceptor {

    // 在请求处理之前进行拦截
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // 放行预检请求，如果是options就直接放行
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 放行静态资源，如图片、CSS、JavaScript等
        if (request.getRequestURI().toString().contains("/images")) {
            return true;
        }

        // 获取请求头中的令牌token
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            // 如果令牌不存在，返回401未授权状态
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
            // throw new RuntimeException("令牌不存在，请先登录");
        } else {
            // 验证令牌的有效性，正确性
            JwtParser parser = Jwts.parser();
            // 解析token，必须和key所生成的token一致
            parser.setSigningKey("skraua2025");
            // 检验token是否正确
            // Jws<Claims> claimsJws =
            parser.parseClaimsJws(token);
            return true;
        }
    }

}

