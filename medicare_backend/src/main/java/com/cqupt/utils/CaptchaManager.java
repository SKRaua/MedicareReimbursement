package com.cqupt.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class CaptchaManager {
    
    private final ConcurrentHashMap<String, CaptchaItem> captchaCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    // 验证码项
    private static class CaptchaItem {
        private final String code;
        private final long expireTime;
        
        public CaptchaItem(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
        
        public String getCode() {
            return code;
        }
    }
    
    public CaptchaManager() {
        // 每分钟清理一次过期的验证码
        scheduler.scheduleAtFixedRate(this::cleanExpired, 1, 1, TimeUnit.MINUTES);
    }
    
    /**
     * 存储验证码，默认5分钟过期
     */
    public void put(String key, String code) {
        put(key, code, 5);
    }
    
    /**
     * 存储验证码，指定过期时间（分钟）
     */
    public void put(String key, String code, int expireMinutes) {
        long expireTime = System.currentTimeMillis() + (expireMinutes * 60 * 1000L);
        captchaCache.put(key, new CaptchaItem(code, expireTime));
    }
    
    /**
     * 验证并移除验证码（一次性使用）
     */
    public boolean validateAndRemove(String key, String inputCode) {
        CaptchaItem item = captchaCache.remove(key);
        if (item == null) {
            return false;
        }
        
        if (item.isExpired()) {
            return false;
        }
        
        return item.getCode().equalsIgnoreCase(inputCode);
    }
    
    /**
     * 只验证不移除
     */
    public boolean validate(String key, String inputCode) {
        CaptchaItem item = captchaCache.get(key);
        if (item == null || item.isExpired()) {
            return false;
        }
        
        return item.getCode().equalsIgnoreCase(inputCode);
    }
    
    /**
     * 移除验证码
     */
    public void remove(String key) {
        captchaCache.remove(key);
    }
    
    /**
     * 清理过期的验证码
     */
    private void cleanExpired() {
        captchaCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    /**
     * 获取当前缓存大小（用于监控）
     */
    public int size() {
        return captchaCache.size();
    }
}