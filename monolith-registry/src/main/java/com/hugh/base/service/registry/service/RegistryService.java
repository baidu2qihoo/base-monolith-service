package com.hugh.base.service.registry.service;

import com.github.benmanes.caffeine.cache.*;
import com.hugh.base.service.registry.entity.ServiceRegistryEntity;
import com.hugh.base.service.registry.repository.ServiceRegistryRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * RegistryService maintains service instances and a local cache for resilience.
 * Cache TTL default 10 minutes for local service list.
 */
@Service
public class RegistryService {

    private final ServiceRegistryRepository repo;
    private Cache<String, List<ServiceRegistryEntity>> serviceListCache;

    public RegistryService(ServiceRegistryRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        serviceListCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(10))
                .maximumSize(10_000)
                .build();
    }

    public ServiceRegistryEntity register(ServiceRegistryEntity entity) {
        entity.setStatus("UP");
        entity.setLastHeartbeat(Instant.now());
        ServiceRegistryEntity saved = repo.save(entity);
        // invalidate cache for service
        String key = cacheKey(entity.getEnv(), entity.getServiceName());
        serviceListCache.invalidate(key);
        return saved;
    }

    public boolean heartbeat(String env, String instanceId) {
        Optional<ServiceRegistryEntity> optional = repo.findByEnvAndInstanceId(env, instanceId);
        if (optional.isEmpty()) {
            return false;
        }
        ServiceRegistryEntity inst = optional.get();
        inst.setLastHeartbeat(Instant.now());
        inst.setStatus("UP");
        repo.save(inst);
        serviceListCache.invalidate(cacheKey(env, inst.getServiceName()));
        return true;
    }

    public List<ServiceRegistryEntity> discover(String env, String serviceName) {
        String key = cacheKey(env, serviceName);
        return serviceListCache.get(key, k -> loadFromDb(env, serviceName));
    }

    private List<ServiceRegistryEntity> loadFromDb(String env, String serviceName) {
        List<ServiceRegistryEntity> list = repo.findByEnvAndServiceNameAndStatus(env, serviceName, "UP");
        // defensive copy
        return new ArrayList<>(list);
    }

    private String cacheKey(String env, String serviceName) {
        return env + ":" + serviceName;
    }

    // cleanup method to mark instances as DOWN if heartbeat is stale (to be scheduled externally)
    public void markStaleInstances(Duration staleAfter) {
        Instant cutoff = Instant.now().minus(staleAfter);
        List<ServiceRegistryEntity> all = repo.findAll();
        for (ServiceRegistryEntity e : all) {
            if (e.getLastHeartbeat() != null && e.getLastHeartbeat().isBefore(cutoff) && "UP".equals(e.getStatus())) {
                e.setStatus("DOWN");
                repo.save(e);
                serviceListCache.invalidate(cacheKey(e.getEnv(), e.getServiceName()));
            }
        }
    }
}
