package com.hugh.base.service.gateway.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory dynamic route registry. Routes are keyed by id.
 * In production this should query the config module (or subscribe to events).
 */
@Service
public class RouteService {

    private final Map<String, RouteDefinition> routes = new ConcurrentHashMap<>();
    private Cache<String, List<RouteDefinition>> prefixCache;

    @PostConstruct
    public void init() {
        prefixCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(10)).build();
        // seed with sample route
        RouteDefinition r = new RouteDefinition();
        r.setId("route-svc");
        r.setPathPrefix("/proxy/svc");
        r.setTargetService("svc");
        routes.put(r.getId(), r);
    }

    public void addOrUpdate(RouteDefinition def) {
        routes.put(def.getId(), def);
        prefixCache.invalidateAll();
    }

    public void remove(String id) {
        routes.remove(id);
        prefixCache.invalidateAll();
    }

    public Optional<RouteDefinition> findByPath(String path) {
        return routes.values().stream().filter(r -> path.startsWith(r.getPathPrefix())).findFirst();
    }

    public Collection<RouteDefinition> list() {
        return routes.values();
    }
}
