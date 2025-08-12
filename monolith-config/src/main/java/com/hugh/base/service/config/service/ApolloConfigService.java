package com.hugh.base.service.config.service;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ApolloConfigService {

    private final ConcurrentHashMap<String, Config> configCache = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        log.info("Initializing Apollo config service");
        // 初始化默认命名空间
        getConfig("application");
    }
    
    /**
     * 获取指定命名空间的配置
     * @param namespace 命名空间
     * @return 配置对象
     */
    public Config getConfig(String namespace) {
        return configCache.computeIfAbsent(namespace, ns -> {
            Config config = ConfigService.getConfig(ns);
            log.info("Loaded config for namespace: {}", ns);
            return config;
        });
    }

    public String getProperty(String namespace, String key, String defaultValue) {
        Config config = getConfig(namespace);
        return config.getProperty(key, defaultValue);
    }

    public void addChangeListener(String namespace, com.ctrip.framework.apollo.model.ConfigChangeListener listener) {
        Config config = getConfig(namespace);
        config.addChangeListener(listener);
    }
    
    /**
     * 获取整型配置属性值
     * @param namespace 命名空间
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public Integer getIntProperty(String namespace, String key, Integer defaultValue) {
        String value = getProperty(namespace, key, null);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.warn("Failed to parse int property: {} with value: {}", key, value);
            }
        }
        return defaultValue;
    }
}
