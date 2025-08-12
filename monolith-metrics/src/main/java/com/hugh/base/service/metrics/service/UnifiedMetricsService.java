package com.hugh.base.service.metrics.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 统一监控指标服务
 * 集成Prometheus指标采集功能
 */
@Slf4j
@Service
public class UnifiedMetricsService {

    private final MeterRegistry meterRegistry;
    private final ConcurrentHashMap<String, AtomicInteger> gauges = new ConcurrentHashMap<>();

    public UnifiedMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * 记录请求计数
     * @param apiName API名称
     * @param success 是否成功
     */
    public void recordRequestCount(String apiName, boolean success) {
        String status = success ? "success" : "failure";
        meterRegistry.counter("api.requests",
                "api", apiName,
                "status", status)
                .increment();
    }

    /**
     * 记录请求耗时
     * @param apiName API名称
     * @param duration 耗时(毫秒)
     */
    public void recordRequestDuration(String apiName, long duration) {
        Timer.Sample sample = Timer.start(meterRegistry);
        sample.stop(Timer.builder("api.request.duration")
                .tag("api", apiName)
                .register(meterRegistry));

        // 同时记录直方图
        meterRegistry.timer("api.request.duration", "api", apiName)
                .record(duration, TimeUnit.MILLISECONDS);
    }

    /**
     * 记录业务指标
     * @param metricName 指标名称
     * @param value 指标值
     */
    public void recordBusinessMetric(String metricName, Number value) {
        // 使用AtomicInteger来支持gauge的动态更新
        AtomicInteger atomicValue = gauges.computeIfAbsent(metricName, 
            k -> {
                AtomicInteger newValue = new AtomicInteger(value.intValue());
                meterRegistry.gauge(metricName, newValue, AtomicInteger::get);
                return newValue;
            });
        atomicValue.set(value.intValue());
        log.debug("Recorded business metric: {} = {}", metricName, value);
    }
}
