package com.hugh.base.service.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app")
public class ControllerHealth {

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(java.util.Map.of("status", "UP"));
    }

    @GetMapping("/info")
    public ResponseEntity<?> info() {
        return ResponseEntity.ok(java.util.Map.of("app", "monolith-app", "env", System.getProperty("APP_ENV", "prod")));
    }
}
