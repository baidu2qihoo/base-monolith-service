package com.hugh.base.service.config.listener;

import com.hugh.base.service.config.event.ConfigChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Listens for config change events and attempts to push them to local subscribers.
 * Implements simple exponential backoff retry.
 */
@Component
public class ConfigChangeListener {
    private static final Logger log = LoggerFactory.getLogger(ConfigChangeListener.class);
    private final ThreadPoolTaskScheduler scheduler;

    public ConfigChangeListener() {
        this.scheduler = new ThreadPoolTaskScheduler();
        this.scheduler.initialize();
    }

    @EventListener
    public void onConfigChange(ConfigChangeEvent evt) {
        log.info("ConfigChangeEvent received: env={} tenant={} service={} app={} key={} ver={}", evt.getEnv(), evt.getTenant(), evt.getService(), evt.getApp(), evt.getKey(), evt.getVersion());
        schedulePush(evt, 0, new AtomicInteger(0));
    }

    private void schedulePush(ConfigChangeEvent evt, long delayMillis, AtomicInteger attempts) {
        ScheduledFuture<?> f = scheduler.schedule(() -> {
            try {
                attempts.incrementAndGet();
                // Simulate push to subscribers (e.g., gateway, sentinel)
                boolean ok = pushToSubscribers(evt);
                if (!ok) {
                    long nextDelay = computeBackoff(attempts.get());
                    log.warn("Push failed, scheduling retry #{} after {}ms", attempts.get(), nextDelay);
                    schedulePush(evt, nextDelay, attempts);
                } else {
                    log.info("Push succeeded for key={} at {}", evt.getKey(), Instant.now());
                }
            } catch (Exception e) {
                long nextDelay = computeBackoff(attempts.get());
                log.error("Push attempt failed: {}", e.getMessage());
                schedulePush(evt, nextDelay, attempts);
            }
        }, Instant.now().plusMillis(delayMillis));
    }

    private boolean pushToSubscribers(ConfigChangeEvent evt) {
        // In real system, this would call MQ or HTTP callbacks. Here we simulate success.
        // You can extend this to call REST endpoints or publish to Kafka/Redis streams.
        return true;
    }

    private long computeBackoff(int attempts) {
        // exponential backoff base 500ms, cap 60s
        long base = 500L;
        long v = base * (1L << Math.min(attempts, 6)); // exponential
        return Math.min(v, 60000L);
    }
}
