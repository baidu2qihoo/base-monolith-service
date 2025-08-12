package com.hugh.base.service.stability.controller;

import com.hugh.base.service.stability.service.StabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stability")
public class StabilityController {

    private final StabilityService stabilityService;

    public StabilityController(StabilityService stabilityService) {
        this.stabilityService = stabilityService;
    }

    @PostMapping("/reload")
    public ResponseEntity<?> reload(@RequestParam String env, @RequestParam String tenant, @RequestParam String service) {
        stabilityService.loadAndApply(env, tenant, service);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @GetMapping("/try")
    public ResponseEntity<?> tryAcquire(@RequestParam String key, @RequestParam(defaultValue = "1") long cost) {
        boolean ok = stabilityService.tryAcquire(key, cost);
        return ResponseEntity.ok(java.util.Map.of("allowed", ok));
    }
}
