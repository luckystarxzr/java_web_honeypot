package com.example.honeypot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {
    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);

    public static void logRequest(String message) {
        logger.info(message);
    }

    public static void logWarning(String message) {
        logger.warn(message);
    }
}
