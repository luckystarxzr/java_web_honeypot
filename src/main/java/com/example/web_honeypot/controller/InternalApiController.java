package com.example.web_honeypot.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/api")
public class InternalApiController {
    
    @GetMapping("/users")
    public String getInternalUsers() {
        return "{ \"internal_users\": [{\"username\":\"admin\",\"role\":\"super_admin\"}] }";
    }
    
    @GetMapping("/config")
    public String getSystemConfig() {
        return "{ \"database\": { \"host\": \"localhost\", \"password\": \"secret123\" } }";
    }
} 