package com.hugh.base.service.metrics.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Common metric tags (env, tenant, service, app)
 */
@Configuration
public class MetricsConfig {

    @Value("${app.env:prod}")
    private String env;

    @Value("${app.tenant:default}")
    private String tenant;

    @Value("${spring.application.name:monolith}")
    private String service;

    @Bean
    public MeterRegistryCustomizer meterRegistryCustomizer() {
        return registry -> registry.config().commonTags("env", env, "tenant", tenant, "service", service);
    }

    @FunctionalInterface
    public interface MeterRegistryCustomizer {
        void customize(MeterRegistry registry);
    }
}
