package com.hugh.base.service.config.controller;

import com.hugh.base.service.config.dto.PublishRequest;
import com.hugh.base.service.config.service.ConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private final ConfigService service;

    public ConfigController(ConfigService service) {
        this.service = service;
    }

    @PostMapping("/publish")
    public ResponseEntity<?> publish(@RequestBody PublishRequest req) {
        Long id = service.publish(req);
        return ResponseEntity.ok().body(java.util.Map.of("id", id));
    }
}
