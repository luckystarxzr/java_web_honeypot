# Web Honeypot System

一个基于Spring Boot的Web蜜罐系统，用于模拟常见Web漏洞，捕获和分析攻击行为。

### 漏洞模拟
- SQL注入
- XSS攻击
- 命令注入
- 目录遍历
- 文件上传漏洞
- CSRF
- XXE

### 攻击检测
- 实时攻击监控
- 攻击类型统计
- IP信誉跟踪
- 攻击详情记录

### 防御机制
- 速率限制
- IP黑名单
- 自动封禁
- 攻击预警

## 环境要求

- JDK 11+
- MongoDB 4.0+
- Redis 6.0+
- Maven 3.6+

## 快速开始

1. **克隆项目**
   bash
   git clone https://github.com/yourusername/web-honeypot.git
   cd web-honeypot

2. **编译运行**
   编译
   mvn clean package
   运行
   java -jar target/web-honeypot-1.0-SNAPSHOT.jar
3.**访问系统**
- 管理面板: http://localhost:9999/admin
- 漏洞测试页面: http://localhost:9999/vulnerable

默认管理员账号:
- 用户名: admin
- 密码: admin123

## 漏洞测试示例
1. **SQL注入测试**
   在用户名输入框中输入：
   admin' OR '1'='1
2.**XSS测试**
  在评论框中输入：
<script>alert('xss')</script>
3. **命令注入测试**
   在命令输入框中输入：
   ls;cat /etc/passwd
4. **SSRF测试**
# 在URL输入框中输入：http://localhost:8080/internal/api/config
5. **XXE测试**
<?xml version="1.0"?>
<!DOCTYPE foo [<!ENTITY xxe SYSTEM "file:///etc/passwd">]>
<foo>&xxe;</foo>
6. **文件上传测试**
尝试上传恶意文件：
shell.jsp
或者
exploit.php
7. **目录遍历测试**
# 在文件路径输入框中输入：
../../../etc/passwd 或者 ..\..\Windows\System32\drivers\etc\hosts

## 测试示例
运行所有测试
mvn test
运行特定测试
mvn test -Dtest=VulnerabilityTest
## 项目结构
src/main/java/com/example/web_honeypot/
├── config/ # 配置类
├── controller/ # 控制器
├── exception/ # 异常处理
├── model/ # 数据模型
├── monitor/ # 监控组件
├── repository/ # 数据访问层
├── service/ # 业务逻辑层
├── util/ # 工具类
└── virtual/ # 虚拟环境模拟