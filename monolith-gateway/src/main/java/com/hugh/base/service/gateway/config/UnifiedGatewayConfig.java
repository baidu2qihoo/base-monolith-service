package com.hugh.base.service.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 统一网关配置
 * 集成API网关功能
 */
@Slf4j
@Configuration
public class UnifiedGatewayConfig {

    /**
     * 开发环境路由配置
     */
    @Bean
    @Profile("dev")
    public RouteLocator devRouteLocator(RouteLocatorBuilder builder) {
        log.info("Initializing dev route locator");
        return builder.routes()
                .route("dev_service_route", r -> r.path("/api/**")
                        .uri("lb://monolith-service"))
                .build();
    }

    /**
     * 测试环境路由配置
     */
    @Bean
    @Profile({"test", "gray"})
    public RouteLocator testRouteLocator(RouteLocatorBuilder builder) {
        log.info("Initializing test/gray route locator");
        return builder.routes()
                .route("test_service_route", r -> r.path("/api/**")
                        .uri("lb://monolith-service"))
                .build();
    }

    /**
     * 生产环境路由配置
     */
    @Bean
    @Profile("prod")
    public RouteLocator prodRouteLocator(RouteLocatorBuilder builder) {
        log.info("Initializing prod route locator");
        return builder.routes()
                .route("prod_service_route", r -> r.path("/api/**")
                        .uri("lb://monolith-service"))
                .build();
    }
}
