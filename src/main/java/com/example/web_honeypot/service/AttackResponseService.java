package com.example.web_honeypot.service;

import com.example.web_honeypot.virtual.VirtualSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class AttackResponseService {
    
    @Autowired
    private VirtualSystem virtualSystem;
    
    @Autowired
    private AttackLogService attackLogService;
    
    public String handleSqlInjection(String query) {
        if (Pattern.compile(".*('|;|--).+").matcher(query).matches()) {
            // 模拟SQL注入成功的响应
            return "{ \"users\": [{\"id\":1,\"username\":\"admin\",\"password\":\"******\"}] }";
        }
        return "{ \"users\": [] }";
    }
    
    public String handleCommandInjection(String command) {
        return virtualSystem.executeCommand(command);
    }
    
    public String handleFileAccess(String path) {
        if (path.contains("../")) {
            // 模拟目录遍历
            return virtualSystem.executeCommand("cat " + path);
        }
        return "Access denied";
    }
    
    public String handleXxe(String xml) {
        if (xml.contains("SYSTEM") || xml.contains("DOCTYPE")) {
            // 模拟XXE漏洞
            String file = extractFilePathFromXXE(xml);
            return virtualSystem.executeCommand("cat " + file);
        }
        return "XML parsed successfully";
    }
    
    private String extractFilePathFromXXE(String xml) {
        // 简单的文件路径提取逻辑
        Pattern pattern = Pattern.compile("SYSTEM\\s+\"file:///([^\"]+)\"");
        var matcher = pattern.matcher(xml);
        return matcher.find() ? matcher.group(1) : "/dev/null";
    }
} 