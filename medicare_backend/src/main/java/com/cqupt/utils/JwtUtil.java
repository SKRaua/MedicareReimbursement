package com.cqupt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String JWT_SECRET = "skraua2025";
    private static final long JWT_EXPIRATION = 24 * 60 * 60 * 1000L; // 24小时过期

    /**
     * 生成JWT Token
     */
    public String generateToken(Integer userId, String username, Integer roleId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("roleId", roleId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setId(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    /**
     * 解析JWT Token
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从Token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("无法从token中获取用户名");
        }
    }

    /**
     * 从Token中获取用户ID
     */
    public Integer getUserIdFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            // 先尝试从claims中获取
            Object userId = claims.get("userId");
            if (userId != null) {
                return (Integer) userId;
            }
            // 如果没有，尝试从Id中获取
            String id = claims.getId();
            return id != null ? Integer.parseInt(id) : null;
        } catch (Exception e) {
            throw new RuntimeException("无法从token中获取用户ID");
        }
    }

    /**
     * 从Token中获取角色ID - 关键方法
     */
    public Integer getRoleIdFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            Object roleId = claims.get("roleId");
            if (roleId != null) {
                return (Integer) roleId;
            }
            throw new RuntimeException("Token中不存在角色信息");
        } catch (Exception e) {
            throw new RuntimeException("无法从token中获取角色信息: " + e.getMessage());
        }
    }
}