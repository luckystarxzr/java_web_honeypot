package com.example.web_honeypot.controller;

import com.example.web_honeypot.service.AttackLogService;
import com.example.web_honeypot.service.AttackResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.*;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

@Controller
@RequestMapping("/vulnerable")
public class VulnerableController {
    
    @Autowired
    private AttackLogService attackLogService;

    @Autowired
    private AttackResponseService attackResponseService;

    // SQL注入漏洞
    @PostMapping("/query")
    @ResponseBody
    public String sqlInjection(@RequestParam String username, HttpServletRequest request) {
        String payload = username;
        attackLogService.logAttack(request, "SQL_INJECTION", "SQL注入尝试", payload);
        return attackResponseService.handleSqlInjection(username);
    }

    // XSS漏洞
    @PostMapping("/comment")
    @ResponseBody
    public String xssVulnerable(@RequestParam String comment, HttpServletRequest request) {
        attackLogService.logAttack(request, "XSS", "XSS攻击尝试", comment);
        return "<div>" + comment + "</div>";
    }

    // 命令注入漏洞
    @PostMapping("/execute")
    @ResponseBody
    public String commandInjection(@RequestParam String command, HttpServletRequest request) {
        attackLogService.logAttack(request, "COMMAND_INJECTION", "命令注入尝试", command);
        return attackResponseService.handleCommandInjection(command);
    }

    // 目录遍历漏洞
    @GetMapping("/download")
    @ResponseBody
    public String directoryTraversal(@RequestParam String file, HttpServletRequest request) {
        attackLogService.logAttack(request, "PATH_TRAVERSAL", "目录遍历尝试", file);
        return "File content of: " + file;
    }

    // 不安全的文件上传
    @PostMapping("/upload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") String fileName, HttpServletRequest request) {
        attackLogService.logAttack(request, "FILE_UPLOAD", "文件上传尝试", fileName);
        return "File uploaded: " + fileName;
    }

    // CSRF漏洞
    @PostMapping("/transfer")
    @ResponseBody
    public String csrfTransfer(@RequestParam String to, @RequestParam Double amount, HttpServletRequest request) {
        attackLogService.logAttack(request, "CSRF", "CSRF转账尝试", "to:" + to + ",amount:" + amount);
        return "Transfer successful: " + amount + " to " + to;
    }

    // 反序列化漏洞
    @PostMapping("/deserialize")
    @ResponseBody
    public String deserialize(@RequestBody byte[] data, HttpServletRequest request) {
        attackLogService.logAttack(request, "DESERIALIZATION", "反序列化尝试", "data length:" + data.length);
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Object obj = ois.readObject();
            return "Deserialized object: " + obj.toString();
        } catch (Exception e) {
            return "Deserialization failed: " + e.getMessage();
        }
    }

    // SSRF漏洞
    @GetMapping("/fetch")
    @ResponseBody
    public String ssrf(@RequestParam String url, HttpServletRequest request) {
        attackLogService.logAttack(request, "SSRF", "SSRF请求尝试", url);
        try {
            URL targetUrl = new URL(url);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(targetUrl.openStream()))) {
                return reader.lines().reduce("", String::concat);
            }
        } catch (Exception e) {
            return "Fetch failed: " + e.getMessage();
        }
    }

    // XXE漏洞
    @PostMapping("/parse-xml")
    @ResponseBody
    public String xxe(@RequestBody String xml, HttpServletRequest request) {
        attackLogService.logAttack(request, "XXE", "XXE解析尝试", xml);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
            return "XML parsed: " + doc.getDocumentElement().getTextContent();
        } catch (Exception e) {
            return "XML parsing failed: " + e.getMessage();
        }
    }

    // 权限绕过
    @GetMapping("/admin/data")
    @ResponseBody
    public String authBypass(@RequestHeader(value = "Role", defaultValue = "") String role, 
                           HttpServletRequest request) {
        attackLogService.logAttack(request, "AUTH_BYPASS", "权限绕过尝试", "role:" + role);
        if ("admin".equals(role)) {
            return "Sensitive data accessed";
        }
        return "Access denied";
    }
} 