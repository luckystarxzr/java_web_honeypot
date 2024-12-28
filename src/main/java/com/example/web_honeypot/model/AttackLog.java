package com.example.web_honeypot.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "attack_logs")
public class AttackLog {
    @Id
    private String id;
    private String ip;
    private String attackType;
    private String details;
    private LocalDateTime timestamp;
    private String payload;
    private String userAgent;
    private String requestHeaders;
    
    public AttackLog(String ip, String attackType, String details, String payload) {
        this.ip = ip;
        this.attackType = attackType;
        this.details = details;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    
    public String getAttackType() { return attackType; }
    public void setAttackType(String attackType) { this.attackType = attackType; }
    
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    
    public String getRequestHeaders() { return requestHeaders; }
    public void setRequestHeaders(String requestHeaders) { this.requestHeaders = requestHeaders; }
} 