package com.hugh.base.service.config;

import com.hugh.base.service.config.PresetConfigService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {
    
    private final PresetConfigService presetConfigService;
    
    public ThreadPoolConfig(PresetConfigService presetConfigService) {
        this.presetConfigService = presetConfigService;
    }

    @Bean(name = "defaultTaskExecutor")
    public ThreadPoolTaskExecutor defaultTaskExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(presetConfigService.getIntProperty("threadpool.core", 10));
        exec.setMaxPoolSize(presetConfigService.getIntProperty("threadpool.max", 50));
        exec.setQueueCapacity(presetConfigService.getIntProperty("threadpool.queue", 100));
        exec.setKeepAliveSeconds(60);
        exec.setThreadNamePrefix("core-exec-");
        exec.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        exec.initialize();
        return exec;
    }
}
