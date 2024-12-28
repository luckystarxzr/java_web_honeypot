package com.example.web_honeypot.service;

import com.example.web_honeypot.model.AttackLog;
import com.example.web_honeypot.repository.AttackLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttackLogService {
    
    private final AttackLogRepository attackLogRepository;
    
    public void logAttack(HttpServletRequest request, String attackType, String details, String payload) {
        AttackLog log = new AttackLog(
            request.getRemoteAddr(),
            attackType,
            details,
            payload
        );
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setRequestHeaders(getHeadersInfo(request));
        
        attackLogRepository.save(log);
    }
    
    public List<AttackLog> getRecentLogs() {
        return attackLogRepository.findByTimestampAfter(
            LocalDateTime.now().minusHours(24)
        );
    }
    
    public long getBlockedIPCount() {
        return attackLogRepository.countDistinctIpByBlocked(true);
    }
    
    public Map<String, Long> getAttackTypeDistribution() {
        return getRecentLogs().stream()
            .collect(Collectors.groupingBy(
                AttackLog::getAttackType,
                Collectors.counting()
            ));
    }
    
    public List<Map<String, Object>> getTopAttackers(int limit) {
        Map<String, Long> ipCounts = getRecentLogs().stream()
            .collect(Collectors.groupingBy(
                AttackLog::getIp,
                Collectors.counting()
            ));
            
        return ipCounts.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(limit)
            .map(entry -> {
                Map<String, Object> result = new HashMap<>();
                result.put("ip", entry.getKey());
                result.put("count", entry.getValue());
                result.put("lastAttack", getLastAttackTime(entry.getKey()));
                return result;
            })
            .collect(Collectors.toList());
    }
    
    private LocalDateTime getLastAttackTime(String ip) {
        return attackLogRepository.findFirstByIpOrderByTimestampDesc(ip)
            .map(AttackLog::getTimestamp)
            .orElse(null);
    }
    
    private String getHeadersInfo(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Collections.list(request.getHeaderNames()).forEach(name ->
            headers.put(name, request.getHeader(name))
        );
        return headers.toString();
    }
    
    public long getAttackCount(String attackType) {
        return attackLogRepository.countByAttackType(attackType);
    }
} 