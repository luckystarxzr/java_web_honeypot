package com.example.web_honeypot;

import com.example.web_honeypot.model.AttackLog;
import com.example.web_honeypot.service.AttackLogService;
import com.example.web_honeypot.service.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HoneypotController {
    
    private final RateLimiter rateLimiter;
    private final AttackLogService attackLogService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, String> login(@RequestParam String username, 
                                   @RequestParam String password, 
                                   HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (!rateLimiter.isAllowed(ip)) {
            return Map.of(
                "status", "failed",
                "message", "Too many requests"
            );
        }
        
        attackLogService.logAttack(request, "LOGIN_ATTEMPT", 
            "Login attempt", "username:" + username);
            
        return Map.of(
            "status", "failed",
            "message", "Invalid credentials"
        );
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        model.addAttribute("todayAttacks", getAttackCount());
        model.addAttribute("blockedIPs", getBlockedIPCount());
        model.addAttribute("sqlInjections", getSqlInjectionCount());
        model.addAttribute("attackLogs", getRecentAttackLogs());
        model.addAttribute("attackTypes", getAttackTypeDistribution());
        model.addAttribute("topAttackers", getTopAttackers());
        
        return "admin";
    }

    private long getAttackCount() {
        return attackLogService.getRecentLogs().size();
    }

    private long getBlockedIPCount() {
        return attackLogService.getBlockedIPCount();
    }

    private long getSqlInjectionCount() {
        return attackLogService.getAttackCount("SQL_INJECTION");
    }

    private List<AttackLog> getRecentAttackLogs() {
        return attackLogService.getRecentLogs();
    }
    
    private Map<String, Long> getAttackTypeDistribution() {
        return attackLogService.getAttackTypeDistribution();
    }
    
    private List<Map<String, Object>> getTopAttackers() {
        return attackLogService.getTopAttackers(10);
    }
    
    @PostMapping("/admin/block-ip")
    @ResponseBody
    public Map<String, String> blockIp(@RequestParam String ip) {
        rateLimiter.blockIp(ip);
        return Map.of(
            "status", "success",
            "message", "IP " + ip + " has been blocked"
        );
    }
}