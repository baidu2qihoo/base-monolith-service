package com.hugh.base.service.registry.scheduler;

import com.hugh.base.service.registry.service.RegistryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Scheduled task to mark stale instances as DOWN.
 */
@Component
public class HeartbeatCleaner {

    private final RegistryService registryService;

    public HeartbeatCleaner(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Scheduled(fixedDelayString = "${registry.cleanup.interval.ms:60000}")
    public void cleanup() {
        // mark instances that haven't heartbeat within 30s as stale
        registryService.markStaleInstances(Duration.ofSeconds(30));
    }
}
