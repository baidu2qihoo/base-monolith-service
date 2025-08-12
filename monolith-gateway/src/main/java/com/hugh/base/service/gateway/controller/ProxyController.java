package com.hugh.base.service.gateway.controller;

import com.hugh.base.service.gateway.ratelimit.TokenBucketRateLimiter;
import com.hugh.base.service.gateway.service.RouteDefinition;
import com.hugh.base.service.gateway.service.RouteService;
import com.hugh.base.service.registry.entity.ServiceRegistryEntity;
import com.hugh.base.service.registry.service.RegistryService;
import com.hugh.base.service.trace.TraceIdUtils;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import java.util.function.Supplier;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

/**
 * Simple proxy controller: forwards requests under /proxy/** to target services based on RouteService.
 * This is synchronous and intended as a minimal gateway example.
 */
@RestController
@RequestMapping("/proxy")
public class ProxyController {

    private static final Logger log = LoggerFactory.getLogger(ProxyController.class);
    private final RouteService routeService;
    @Autowired
    private RegistryService registryService;
    private final TokenBucketRateLimiter limiter;
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final Random random = new Random();
    private final CircuitBreaker circuitBreaker;
    
    // 监控指标
    private final Counter requestCounter;
    private final Counter errorCounter;
    private final Timer requestTimer;
    
    @Value("${gateway.target.env:dev}")
    private String targetEnv;

    public ProxyController(RouteService routeService, 
                          CircuitBreaker circuitBreaker,
                          MeterRegistry meterRegistry) {
        this.routeService = routeService;
        this.circuitBreaker = circuitBreaker;
        this.limiter = new TokenBucketRateLimiter();
        // sample bucket: 100 qps per key
        this.limiter.defineBucket("default", 100, 100, 1000);
        
        // 初始化监控指标
        this.requestCounter = Counter.builder("gateway.requests.total")
                .description("Total number of requests")
                .register(meterRegistry);
        this.errorCounter = Counter.builder("gateway.errors.total")
                .description("Total number of errors")
                .register(meterRegistry);
        this.requestTimer = Timer.builder("gateway.request.duration")
                .description("Request duration")
                .register(meterRegistry);
    }

    private HttpUriRequestBase createHttpRequest(HttpServletRequest request, String target, byte[] body) {
        String method = request.getMethod();
        String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        
        switch (method.toUpperCase()) {
            case "GET":
                return new HttpGet(target + queryString);
            case "POST":
                HttpPost post = new HttpPost(target + queryString);
                if (body != null) {
                    post.setEntity(new org.apache.hc.core5.http.io.entity.ByteArrayEntity(
                            body, 
                            ContentType.parse(request.getContentType() == null ? 
                                    MediaType.APPLICATION_OCTET_STREAM_VALUE : 
                                    request.getContentType())));
                }
                return post;
            case "PUT":
                HttpPut put = new HttpPut(target + queryString);
                if (body != null) {
                    put.setEntity(new org.apache.hc.core5.http.io.entity.ByteArrayEntity(
                            body, 
                            ContentType.parse(request.getContentType() == null ? 
                                    MediaType.APPLICATION_OCTET_STREAM_VALUE : 
                                    request.getContentType())));
                }
                return put;
            case "DELETE":
                return new HttpDelete(target + queryString);
            default:
                throw new IllegalArgumentException("Method not allowed: " + method);
        }
    }
    
    private void copyHeaders(HttpServletRequest request, HttpUriRequestBase proxyReq) {
        var headerNames = java.util.Collections.list(request.getHeaderNames());
        for (String hn : headerNames) {
            if (hn.equalsIgnoreCase("content-length")) continue;
            proxyReq.addHeader(hn, request.getHeader(hn));
        }
    }

    @RequestMapping("/**")
    public Object proxy(HttpServletRequest request, @RequestBody(required = false) byte[] body) throws Exception {
        requestCounter.increment();
        Timer.Sample sample = Timer.start();
        
        try {
            String path = request.getRequestURI(); // e.g., /proxy/svc/...
            Optional<RouteDefinition> r = routeService.findByPath(path);
            if (r.isEmpty()) {
                errorCounter.increment();
                return org.springframework.http.ResponseEntity.status(404).body("No route");
            }
            RouteDefinition def = r.get();
            String tenant = request.getHeader("X-Tenant") == null ? "default" : request.getHeader("X-Tenant");
            String key = tenant + ":" + def.getId();
            if (!limiter.tryConsume(key, 1)) {
                log.warn("Rate limit exceeded for key={}", key);
                errorCounter.increment();
                return org.springframework.http.ResponseEntity.status(429).body("Too Many Requests");
            }

            List<ServiceRegistryEntity> instances = registryService.discover(targetEnv, def.getTargetService()); // 使用配置的环境
            if (instances.isEmpty()) {
                errorCounter.increment();
                return org.springframework.http.ResponseEntity.status(503).body("No backend available");
            }
            ServiceRegistryEntity inst = instances.get(random.nextInt(instances.size()));
            String target = String.format("http://%s:%d%s", inst.getHost(), inst.getPort(), path.substring(def.getPathPrefix().length()));
            TraceIdUtils.getOrCreate(); // ensure traceId present

            // 创建HTTP请求
            HttpUriRequestBase proxyReq = createHttpRequest(request, target, body);
            proxyReq.addHeader("X-Trace-Id", TraceIdUtils.getOrCreate());

            // 复制请求头
            copyHeaders(request, proxyReq);

            // 使用断路器包装HTTP调用
            Supplier<Object> decoratedSupplier = CircuitBreaker
                    .decorateSupplier(circuitBreaker, () -> {
                        try (var resp = httpClient.execute(proxyReq)) {
                            int status = resp.getCode();
                            HttpEntity ent = resp.getEntity();
                            byte[] bytes = ent == null ? new byte[0] : EntityUtils.toByteArray(ent);
                            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                            for (Header h : resp.getHeaders()) {
                                headers.add(h.getName(), h.getValue());
                            }
                            return new org.springframework.http.ResponseEntity<>(bytes, headers, org.springframework.http.HttpStatus.valueOf(status));
                        } catch (Exception e) {
                            log.error("Proxy error", e);
                            throw new RuntimeException(e);
                        }
                    });

            try {
                Object result = decoratedSupplier.get();
                sample.stop(requestTimer);
                return result;
            } catch (CallNotPermittedException e) {
                log.error("Circuit breaker is open for target: {}", target);
                errorCounter.increment();
                sample.stop(requestTimer);
                return org.springframework.http.ResponseEntity.status(503).body("Service Unavailable - Circuit Breaker Open");
            } catch (Exception e) {
                log.error("Proxy error", e);
                errorCounter.increment();
                sample.stop(requestTimer);
                return org.springframework.http.ResponseEntity.status(502).body("Bad Gateway");
            }
        } catch (Exception e) {
            errorCounter.increment();
            sample.stop(requestTimer);
            throw e;
        }
    }
}
