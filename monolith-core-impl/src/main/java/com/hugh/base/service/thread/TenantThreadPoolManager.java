package com.hugh.base.service.thread;

import org.springframework.stereotype.Component;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ConcurrentMap;

/**
 * Manages per-tenant thread pools. Pools are bounded by global limits.
 */
@Component
public class TenantThreadPoolManager {

    private final ConcurrentMap<String, ThreadPoolTaskExecutor> pools = new ConcurrentHashMap<>();

    public ThreadPoolTaskExecutor getOrCreate(String tenantId, int core, int max, int queue) {
        return pools.computeIfAbsent(tenantId, t -> {
            ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
            exec.setCorePoolSize(core);
            exec.setMaxPoolSize(max);
            exec.setQueueCapacity(queue);
            exec.setKeepAliveSeconds(60);
            exec.setThreadNamePrefix("tenant-" + tenantId + "-");
            exec.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
            exec.initialize();
            return exec;
        });
    }
}
