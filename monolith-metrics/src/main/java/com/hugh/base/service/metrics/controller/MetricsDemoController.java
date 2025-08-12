package com.hugh.base.service.metrics.controller;

import io.micrometer.core.instrument.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/metrics")
public class MetricsDemoController {

    private final Counter requestCounter;
    private final Timer processingTimer;
    private final Gauge queueGauge;
    private volatile int queueSize = 0;

    public MetricsDemoController(MeterRegistry registry) {
        this.requestCounter = Counter.builder("http.requests.total").description("Total HTTP requests").register(registry);
        this.processingTimer = Timer.builder("http.request.latency").publishPercentiles(0.5,0.95,0.99).register(registry);
        this.queueGauge = Gauge.builder("app.queue.size", this, MetricsDemoController::getQueueSize).register(registry);
    }

    @GetMapping("/ping")
    public String ping() {
        requestCounter.increment();
        return "pong";
    }

    @GetMapping("/work")
    public String work(@RequestParam(defaultValue = "100") long ms) {
        requestCounter.increment();
        try {
            return processingTimer.recordCallable(() -> {
                try {
                    Thread.sleep(ms);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "done:" + ms;
            });
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }

    @PostMapping("/enqueue")
    public String enqueue(@RequestParam(defaultValue = "1") int n) {
        queueSize += n;
        return "enqueued:" + n;
    }

    @PostMapping("/dequeue")
    public String dequeue(@RequestParam(defaultValue = "1") int n) {
        queueSize = Math.max(0, queueSize - n);
        return "dequeued:" + n;
    }

    public int getQueueSize() { return queueSize; }
}
