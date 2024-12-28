package com.example.web_honeypot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {
    
    private final AttackAnalysisService analysisService;
    
    public void checkAndAlert() {
        // 检查高风险IP
        analysisService.identifyPotentialThreats()
            .forEach(this::sendAlert);
            
        // 检查攻击频率
        analysisService.getAttackerIPDistribution()
            .forEach((ip, count) -> {
                if (count > 100) { // 设置阈值
                    sendAlert("IP " + ip + " 在短时间内发起了大量攻击");
                }
            });
    }
    
    private void sendAlert(String message) {
        // 实现告警逻辑，可以是邮件、短信、webhook等
        System.out.println("ALERT: " + message);
    }
} 