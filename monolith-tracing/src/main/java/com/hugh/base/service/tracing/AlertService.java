package com.hugh.base.service.tracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertService {
    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

    public void sendLatencyAlert(String traceId, long latencyMs, long thresholdMs) {
        if (latencyMs > thresholdMs) {
            log.warn("[ALERT] TraceId: {} latency {}ms exceeds threshold {}ms", traceId, latencyMs, thresholdMs);
            // 这里可以扩展调用钉钉/飞书/邮件告警
        }
    }
}
