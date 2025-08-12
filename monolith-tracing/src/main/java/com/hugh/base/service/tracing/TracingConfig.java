package com.hugh.base.service.tracing;

import org.slf4j.MDC;
import java.util.UUID;

public class TracingConfig {

    private static double sampleRate = 1.0; // 默认全采样

    public static void setSampleRate(double rate) {
        if (rate >= 0 && rate <= 1) {
            sampleRate = rate;
        }
    }

    public static String initTrace() {
        if (Math.random() <= sampleRate) {
            String traceId = UUID.randomUUID().toString();
            MDC.put("traceId", traceId);
            return traceId;
        }
        return null;
    }

    public static String getTraceId() {
        return MDC.get("traceId");
    }

    public static void clearTrace() {
        MDC.remove("traceId");
    }
}
