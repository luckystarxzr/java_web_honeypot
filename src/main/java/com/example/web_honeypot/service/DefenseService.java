package com.example.web_honeypot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DefenseService {
    
    private final RateLimiter rateLimiter;
    private final AttackAnalysisService analysisService;
    
    public void applyDefenseStrategies() {
        // 自动封禁高风险IP
        analysisService.identifyPotentialThreats()
            .forEach(rateLimiter::blockIp);
            
        // 调整速率限制
        adjustRateLimits();
    }
    
    private void adjustRateLimits() {
        // 根据攻击情况动态调整限制
        Map<String, Long> distribution = analysisService.getAttackTypeDistribution();
        if (distribution.values().stream().mapToLong(l -> l).sum() > 1000) {
            rateLimiter.setStrictMode(true);
        }
    }
} 