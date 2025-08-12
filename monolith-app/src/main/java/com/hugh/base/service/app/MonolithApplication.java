package com.hugh.base.service.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.hugh.base.service")
@EnableScheduling
@EnableDiscoveryClient
public class MonolithApplication {
    public static void main(String[] args) {
        // 设置默认环境变量（如果未设置）
        setDefaultSystemProperties();
        
        SpringApplication.run(MonolithApplication.class, args);
    }
    
    private static void setDefaultSystemProperties() {
        // 如果没有设置环境变量，则使用默认值
        if (System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", "dev");
        }
        
        if (System.getProperty("server.port") == null) {
            System.setProperty("server.port", "8080");
        }
    }
}
