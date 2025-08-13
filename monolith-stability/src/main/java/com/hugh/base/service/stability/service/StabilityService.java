package com.hugh.base.service.stability.service;

import com.hugh.base.service.stability.entity.RuleEntity;
import com.hugh.base.service.stability.repository.RuleRepository;
import com.hugh.base.service.gateway.ratelimit.TokenBucketRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StabilityService loads flow/degrade rules from DB and applies to local engines.
 * This is a simplified stand-in for Sentinel integration: it manages an in-memory set of token buckets
 * and degrade flags per resource.
 */
@Service
public class StabilityService {

    private static final Logger log = LoggerFactory.getLogger(StabilityService.class);

    private final RuleRepository ruleRepository;
    private final TokenBucketRateLimiter limiter = new TokenBucketRateLimiter();

    // simple in-memory degrade flags: resource -> boolean
    private final Map<String, Boolean> degradeFlags = new ConcurrentHashMap<>();

    public StabilityService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @PostConstruct
    public void init() {
        log.info("StabilityService initialized");
    }

    public void loadAndApply(String env, String tenantId, String serviceName) {
        List<RuleEntity> rules = ruleRepository.findByEnvAndTenantIdAndServiceNameAndActive(env, tenantId, serviceName, true);
        log.info("Loading {} rules for {}/{}/{}", rules.size(), env, tenantId, serviceName);
        for (RuleEntity r : rules) {
            try {
                applyRule(r);
            } catch (Exception e) {
                log.error("Failed to apply rule {}: {}", r.getRuleKey(), e.getMessage());
            }
        }
    }

    private void applyRule(RuleEntity r) {
        switch (r.getRuleType()) {
            case "FLOW":
                // expect rule_json like: {"key":"tenant:route","capacity":100,"refill":100,"periodMs":1000}
                var json = r.getRuleJson();
                var map = parseJsonToMap(json);
                String key = (String) map.getOrDefault("key", r.getRuleKey());
                long cap = ((Number) map.getOrDefault("capacity", 100)).longValue();
                long refill = ((Number) map.getOrDefault("refill", 100)).longValue();
                long period = ((Number) map.getOrDefault("periodMs", 1000)).longValue();
                limiter.defineBucket(key, cap, refill, period);
                log.info("Applied FLOW rule for key={}, cap={}, refill={}, periodMs={}", key, cap, refill, period);
                break;
            case "DEGRADE":
                // expect {"key":"resource","degrade":true}
                var m2 = parseJsonToMap(r.getRuleJson());
                String rk = (String) m2.getOrDefault("key", r.getRuleKey());
                Boolean flag = (Boolean) m2.getOrDefault("degrade", Boolean.TRUE);
                degradeFlags.put(rk, flag);
                log.info("Applied DEGRADE rule for key={}, degrade={}", rk, flag);
                break;
            default:
                log.warn("Unsupported rule type: {}", r.getRuleType());
        }
    }

    private Map<String, Object> parseJsonToMap(String json) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, Map.class);
        } catch (Exception e) {
            log.error("parseJsonToMap error", e);
            return java.util.Collections.emptyMap();
        }
    }

    public boolean tryAcquire(String key, long cost) {
        // check degrade first
        if (degradeFlags.getOrDefault(key, false)) {
            return false;
        }
        return limiter.tryConsume(key, cost);
    }
}
