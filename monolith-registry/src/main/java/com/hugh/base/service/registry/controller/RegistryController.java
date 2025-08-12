package com.hugh.base.service.registry.controller;

import com.hugh.base.service.registry.dto.RegisterRequest;
import com.hugh.base.service.registry.dto.HeartbeatRequest;
import com.hugh.base.service.registry.entity.ServiceRegistryEntity;
import com.hugh.base.service.registry.service.RegistryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Simple registry HTTP endpoints for register/heartbeat/discover.
 */
@RestController
@RequestMapping("/api/registry")
public class RegistryController {

    private final RegistryService service;

    public RegistryController(RegistryService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        ServiceRegistryEntity e = new ServiceRegistryEntity();
        e.setEnv(req.getEnv());
        e.setTenantId(req.getTenantId());
        e.setServiceName(req.getServiceName());
        e.setInstanceId(req.getInstanceId());
        e.setHost(req.getHost());
        e.setPort(req.getPort());
        e.setMetadata(req.getMetadata());
        ServiceRegistryEntity saved = service.register(e);
        return ResponseEntity.ok(java.util.Map.of("id", saved.getId()));
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<?> heartbeat(@RequestBody HeartbeatRequest req) {
        boolean ok = service.heartbeat(req.getEnv(), req.getInstanceId());
        return ResponseEntity.ok(java.util.Map.of("ok", ok));
    }

    @GetMapping("/discover")
    public ResponseEntity<?> discover(@RequestParam String env, @RequestParam String serviceName) {
        List<ServiceRegistryEntity> list = service.discover(env, serviceName);
        return ResponseEntity.ok(list);
    }
}
