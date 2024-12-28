package com.example.web_honeypot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import java.util.HashMap;
import java.util.Map;
import com.example.web_honeypot.virtual.VirtualSystem;

@Configuration
public class VirtualEnvironmentConfig {
    
    @Bean
    public VirtualSystem virtualSystem() {
        return new VirtualSystem(
            "Ubuntu",
            "20.04",
            createVirtualFileSystem(),
            createVirtualProcesses(),
            createVirtualUsers()
        );
    }
    
    private Map<String, String> createVirtualFileSystem() {
        Map<String, String> files = new HashMap<>();
        files.put("/etc/passwd", "root:x:0:0:root:/root:/bin/bash\n" +
                                "mysql:x:111:112:MySQL Server,,,:/var/lib/mysql:/bin/false\n" +
                                "admin:x:1000:1000:admin:/home/admin:/bin/bash");
        files.put("/etc/shadow", "root:$6$xyz...:/0:99999:7:::");
        files.put("/var/www/html/index.php", "<?php phpinfo(); ?>");
        files.put("/home/admin/sensitive.txt", "DB_PASSWORD=secret123\nAPI_KEY=abcdef123456");
        return files;
    }
    
    private Map<Integer, String> createVirtualProcesses() {
        Map<Integer, String> processes = new HashMap<>();
        processes.put(1, "systemd");
        processes.put(80, "apache2");
        processes.put(3306, "mysqld");
        processes.put(6379, "redis-server");
        return processes;
    }
    
    private Map<String, String[]> createVirtualUsers() {
        Map<String, String[]> users = new HashMap<>();
        users.put("root", new String[]{"root", "sudo"});
        users.put("admin", new String[]{"sudo", "www-data"});
        users.put("mysql", new String[]{"mysql"});
        return users;
    }
} 