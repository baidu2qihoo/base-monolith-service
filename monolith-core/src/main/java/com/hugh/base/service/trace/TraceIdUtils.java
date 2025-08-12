package com.hugh.base.service.trace;

import org.slf4j.MDC;

import java.util.UUID;

public class TraceIdUtils {
    public static final String TRACE_ID = "traceId";

    public static String getOrCreate() {
        String t = MDC.get(TRACE_ID);
        if (t == null) {
            t = UUID.randomUUID().toString().replace("-", "");
            MDC.put(TRACE_ID, t);
        }
        return t;
    }

    public static void set(String traceId) {
        if (traceId != null){
            MDC.put(TRACE_ID, traceId);
        }
    }

    public static void clear() {
        MDC.remove(TRACE_ID);
    }
}
