package com.hugh.base.service.gateway.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Very small token bucket implementation per key (e.g., tenant+route).
 * Not distributed. For distributed rate limiting use Redis or Sentinel.
 */
public class TokenBucketRateLimiter {

    private static class Bucket {
        final long capacity;
        final long refillTokens;
        final long refillPeriodMillis;
        AtomicLong tokens;
        long lastRefill;

        Bucket(long capacity, long refillTokens, long refillPeriodMillis) {
            this.capacity = capacity;
            this.refillTokens = refillTokens;
            this.refillPeriodMillis = refillPeriodMillis;
            this.tokens = new AtomicLong(capacity);
            this.lastRefill = System.currentTimeMillis();
        }
    }

    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public void defineBucket(String key, long capacity, long refillTokens, long refillPeriodMillis) {
        buckets.put(key, new Bucket(capacity, refillTokens, refillPeriodMillis));
    }

    public boolean tryConsume(String key, long cost) {
        Bucket b = buckets.get(key);
        if (b == null) return true; // no limit
        refill(b);
        while (true) {
            long available = b.tokens.get();
            if (available < cost) return false;
            if (b.tokens.compareAndSet(available, available - cost)) return true;
        }
    }

    private void refill(Bucket b) {
        long now = System.currentTimeMillis();
        long periods = (now - b.lastRefill) / b.refillPeriodMillis;
        if (periods > 0) {
            long toAdd = periods * b.refillTokens;
            long newTokens = Math.min(b.capacity, b.tokens.get() + toAdd);
            b.tokens.set(newTokens);
            b.lastRefill = now;
        }
    }
}
