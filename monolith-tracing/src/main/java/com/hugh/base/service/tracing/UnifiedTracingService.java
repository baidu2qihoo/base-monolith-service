package com.hugh.base.service.tracing;

import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.stereotype.Service;

/**
 * 统一链路追踪服务
 * 集成Skywalking链路追踪功能
 */
@Slf4j
@Service
public class UnifiedTracingService {

    /**
     * 创建追踪操作
     * @param operationName 操作名称
     */
    @Trace
    public void createTraceOperation(String operationName) {
        log.debug("Creating trace operation: {}", operationName);
    }

    /**
     * 获取当前追踪ID
     * @return 追踪ID
     */
    public String getTraceId() {
        return TraceContext.traceId();
    }

    /**
     * 添加追踪标签
     * @param key 标签键
     * @param value 标签值
     */
    @Trace
    public void addTraceTag(String key, String value) {
        // 在实际Skywalking集成中，这里会添加标签到当前追踪上下文
        log.debug("Adding trace tag: {} = {}", key, value);
    }
}
