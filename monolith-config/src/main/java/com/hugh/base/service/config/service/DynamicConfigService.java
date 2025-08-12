package com.hugh.base.service.config.service;

import com.hugh.base.service.config.PresetConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态配置服务
 * 支持配置热更新和平滑切换
 */
@Service
public class DynamicConfigService {

    private static final Logger logger = LoggerFactory.getLogger(DynamicConfigService.class);

    private final PresetConfigService presetConfigService;

    // 动态配置存储
    private final ConcurrentHashMap<String, Object> dynamicConfig = new ConcurrentHashMap<>();

    public DynamicConfigService(PresetConfigService presetConfigService) {
        this.presetConfigService = presetConfigService;
    }

    @PostConstruct
    public void init() {
        logger.info("动态配置服务初始化完成");
    }

    /**
     * 更新配置项
     * @param key 配置键
     * @param value 配置值
     */
    public void updateConfig(String key, Object value) {
        Object oldValue = dynamicConfig.put(key, value);
        // 更新预置配置服务中的缓存
        presetConfigService.updateCachedProperty(key, value);
        logger.info("配置更新: {} = {} (原值: {})", key, value, oldValue);
    }

    /**
     * 批量更新配置
     * @param configMap 配置映射
     */
    public void updateConfigs(Map<String, Object> configMap) {
        configMap.forEach(this::updateConfig);
        logger.info("批量配置更新完成，共更新 {} 个配置项", configMap.size());
    }

    /**
     * 获取配置值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public Object getConfig(String key, Object defaultValue) {
        return dynamicConfig.getOrDefault(key, defaultValue);
    }

    /**
     * 获取所有动态配置
     * @return 配置映射
     */
    public Map<String, Object> getAllConfigs() {
        return new ConcurrentHashMap<>(dynamicConfig);
    }
}
