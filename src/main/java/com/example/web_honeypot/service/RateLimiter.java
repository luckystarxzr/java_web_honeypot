package com.example.web_honeypot.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.example.web_honeypot.Config;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConfigurationProperties(prefix = "honeypot.security.rate-limit")
public class RateLimiter {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Setter
    private boolean enabled = true;
    
    @Setter
    private boolean strictMode = false;
    
    @Setter
    private int maxRequests = 100;
    
    @Setter
    private int windowSize = 60;
    
    @Setter
    private int strictModeThreshold = 10;
    
    public boolean isAllowed(String ip) {
        if (!enabled) {
            return true;
        }
        
        if (isBlocked(ip)) {
            log.warn("IP {} is blocked", ip);
            return false;
        }
        
        String key = "rate:" + ip;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, windowSize, TimeUnit.SECONDS);
        }
        
        int limit = strictMode ? strictModeThreshold : maxRequests;
        boolean allowed = count <= limit;
        
        if (!allowed) {
            log.warn("Rate limit exceeded for IP: {}, count: {}", ip, count);
        }
        
        return allowed;
    }
    
    public void blockIp(String ip) {
        redisTemplate.opsForSet().add("blocked_ips", ip);
        log.info("Blocked IP: {}", ip);
    }
    
    public boolean isBlocked(String ip) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember("blocked_ips", ip));
    }
    
    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
        log.info("Rate limiter strict mode set to: {}", strictMode);
    }
} 