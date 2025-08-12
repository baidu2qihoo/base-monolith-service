package com.hugh.base.service.app;

import com.ctrip.framework.apollo.core.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 统一配置类
 * 提供全局配置和Bean定义
 */
@Configuration
public class UnifiedConfig {
    
    /**
     * 设置Apollo默认集群
     */
    @Bean
    @ConditionalOnMissingBean
    public String apolloCluster() {
        System.setProperty(Constants.APOLLO_CLUSTER_KEY, "default");
        return "default";
    }
}
