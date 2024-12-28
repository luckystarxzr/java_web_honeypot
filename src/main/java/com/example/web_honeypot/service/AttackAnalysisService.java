package com.example.web_honeypot.service;

import com.example.web_honeypot.model.AttackLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttackAnalysisService {
    
    private final AttackLogService attackLogService;
    
    public Map<String, Long> getAttackTypeDistribution() {
        List<AttackLog> logs = attackLogService.getRecentLogs();
        return logs.stream()
            .collect(Collectors.groupingBy(
                AttackLog::getAttackType,
                Collectors.counting()
            ));
    }
    
    public Map<String, Long> getAttackerIPDistribution() {
        List<AttackLog> logs = attackLogService.getRecentLogs();
        return logs.stream()
            .collect(Collectors.groupingBy(
                AttackLog::getIp,
                Collectors.counting()
            ));
    }
    
    public List<String> identifyPotentialThreats() {
        return attackLogService.getRecentLogs().stream()
            .filter(this::isHighRiskAttack)
            .map(AttackLog::getIp)
            .distinct()
            .collect(Collectors.toList());
    }
    
    private boolean isHighRiskAttack(AttackLog log) {
        // 判断高风险攻击的逻辑
        return log.getAttackType().equals("COMMAND_INJECTION") ||
               log.getAttackType().equals("SQL_INJECTION") ||
               log.getPayload().contains("rm -rf") ||
               log.getPayload().contains("DROP TABLE");
    }
} 