package com.example.web_honeypot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.web_honeypot.repository")
@EnableMongoAuditing
public class MongoConfig {
    // MongoDB配置
} 