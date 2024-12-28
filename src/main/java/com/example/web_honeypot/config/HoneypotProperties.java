package com.example.web_honeypot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "honeypot")
public class HoneypotProperties {
    private VirtualEnvironment virtualEnvironment = new VirtualEnvironment();
    private Security security = new Security();
    private Monitoring monitoring = new Monitoring();
    
    @Data
    public static class VirtualEnvironment {
        private String os = "Ubuntu";
        private String version = "20.04";
        private int maxProcesses = 100;
        private String maxFileSize = "10MB";
        private String[] allowedCommands = {"ls", "cat", "ps", "pwd"};
    }
    
    @Data
    public static class Security {
        private RateLimit rateLimit = new RateLimit();
        private Alert alert = new Alert();
        private Blacklist blacklist = new Blacklist();
    }
    
    @Data
    public static class RateLimit {
        private boolean enabled = true;
        private int maxRequests = 100;
        private int windowSize = 60;
        private int strictModeThreshold = 1000;
    }
    
    @Data
    public static class Alert {
        private boolean enabled = true;
        private String email;
        private String webhook;
        private Threshold threshold = new Threshold();
    }
    
    @Data
    public static class Threshold {
        private int attackCount = 100;
        private int ipBlock = 50;
    }
    
    @Data
    public static class Blacklist {
        private boolean enabled = true;
        private int maxSize = 10000;
        private int expireHours = 24;
    }
    
    @Data
    public static class Monitoring {
        private boolean enabled = true;
        private long interval = 60000;
        private Metrics metrics = new Metrics();
        private Logging logging = new Logging();
    }
    
    @Data
    public static class Metrics {
        private boolean enabled = true;
    }
    
    @Data
    public static class Logging {
        private String path = "logs/honeypot.log";
        private String level = "INFO";
        private String maxSize = "100MB";
        private int maxHistory = 30;
    }
} 