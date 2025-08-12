package com.hugh.base.service.stability.listener;

import com.hugh.base.service.config.event.ConfigChangeEvent;
import com.hugh.base.service.stability.service.StabilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens for configuration changes and reloads rules when relevant.
 */
@Component
public class ConfigChangeRuleReloader {
    private static final Logger log = LoggerFactory.getLogger(ConfigChangeRuleReloader.class);
    private final StabilityService stabilityService;

    public ConfigChangeRuleReloader(StabilityService stabilityService) {
        this.stabilityService = stabilityService;
    }

    @EventListener
    public void onConfigChange(ConfigChangeEvent evt) {
        log.info("ConfigChangeEvent received in stability module: {} {}/{}", evt.getKey(), evt.getTenant(), evt.getService());
        // For simplicity, trigger reload for the service affected
        stabilityService.loadAndApply(evt.getEnv(), evt.getTenant(), evt.getService());
    }
}
