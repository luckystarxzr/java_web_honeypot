package com.example.web_honeypot.util;

import java.util.regex.Pattern;

public class AttackPatternUtil {
    private static final Pattern SQL_INJECTION_PATTERN = 
        Pattern.compile(".*('|;|--|\\/\\*|\\*\\/|xp_).*");
    
    private static final Pattern XSS_PATTERN = 
        Pattern.compile(".*(<script|javascript:|onerror=|onload=).*");
    
    private static final Pattern PATH_TRAVERSAL_PATTERN = 
        Pattern.compile(".*(\\.\\./|\\.\\.\\\\\"|\\\\\\.\\.\\\\).*");
    
    public static boolean isSqlInjectionAttempt(String input) {
        return SQL_INJECTION_PATTERN.matcher(input.toLowerCase()).matches();
    }
    
    public static boolean isXssAttempt(String input) {
        return XSS_PATTERN.matcher(input.toLowerCase()).matches();
    }
    
    public static boolean isPathTraversalAttempt(String input) {
        return PATH_TRAVERSAL_PATTERN.matcher(input).matches();
    }
    
    public static boolean isCommandInjectionAttempt(String input) {
        return input.contains(";") || input.contains("|") || input.contains("&&");
    }
} 