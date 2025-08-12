package com.hugh.base.service.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 预置配置服务
 * 支持预置配置模板和配置缓存机制
 */
@Service
public class PresetConfigService {

    private static final Logger logger = LoggerFactory.getLogger(PresetConfigService.class);

    // 配置缓存
    private final ConcurrentHashMap<String, Object> configCache = new ConcurrentHashMap<>();

    // 预置配置
    private Properties presetProperties = new Properties();

    @PostConstruct
    public void init() {
        loadPresetConfig();
    }

    /**
     * 加载预置配置
     */
    private void loadPresetConfig() {
        try {
            ClassPathResource resource = new ClassPathResource("preset-config.properties");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    presetProperties.load(is);
                    logger.info("预置配置加载成功，共加载 {} 个配置项", presetProperties.size());
                }
            } else {
                logger.warn("未找到预置配置文件 preset-config.properties，使用默认配置");
                // 设置一些默认配置
                presetProperties.setProperty("app.env", "default");
                presetProperties.setProperty("server.port", "8080");
                presetProperties.setProperty("spring.datasource.maximum-pool-size", "10");
                presetProperties.setProperty("threadpool.core", "10");
                presetProperties.setProperty("threadpool.max", "50");
            }
        } catch (IOException e) {
            logger.error("加载预置配置失败", e);
        }
    }

    /**
     * 获取配置值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public String getProperty(String key, String defaultValue) {
        // 先从缓存中获取
        Object cachedValue = configCache.get(key);
        if (cachedValue != null) {
            return cachedValue.toString();
        }

        // 从环境变量获取
        String envValue = System.getenv(key.replace(".", "_").toUpperCase());
        if (envValue != null) {
            configCache.put(key, envValue);
            return envValue;
        }

        // 从系统属性获取
        String sysValue = System.getProperty(key);
        if (sysValue != null) {
            configCache.put(key, sysValue);
            return sysValue;
        }

        // 从预置配置获取
        String presetValue = presetProperties.getProperty(key);
        if (presetValue != null) {
            configCache.put(key, presetValue);
            return presetValue;
        }

        return defaultValue;
    }

    /**
     * 获取整型配置值
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("配置项 {} 的值 {} 不是有效的整数，使用默认值 {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 获取布尔型配置值
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }

    /**
     * 更新缓存中的配置值
     */
    public void updateCachedProperty(String key, Object value) {
        configCache.put(key, value);
        logger.debug("更新配置缓存: {} = {}", key, value);
    }

    /**
     * 清除配置缓存
     */
    public void clearCache() {
        configCache.clear();
        logger.info("配置缓存已清除");
    }
}
