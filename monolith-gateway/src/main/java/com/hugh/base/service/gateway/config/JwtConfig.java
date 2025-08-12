package com.hugh.base.service.gateway.config;

import com.hugh.base.service.gateway.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    
    @Value("${jwt.secret:ChangeThisSecret}")
    private String jwtSecret;
    
    @PostConstruct
    public void init() {
        // 初始化JWT密钥
        JwtUtil.setSecret(jwtSecret);
    }
}