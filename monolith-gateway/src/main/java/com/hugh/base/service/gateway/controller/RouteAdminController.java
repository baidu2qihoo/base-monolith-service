package com.hugh.base.service.gateway.controller;

import com.hugh.base.service.gateway.service.RouteDefinition;
import com.hugh.base.service.gateway.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gateway/routes")
public class RouteAdminController {

    private final RouteService routeService;

    public RouteAdminController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/upsert")
    public ResponseEntity<?> upsert(@RequestBody RouteDefinition def) {
        routeService.addOrUpdate(def);
        return ResponseEntity.ok().body(java.util.Map.of("ok", true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        routeService.remove(id);
        return ResponseEntity.ok().body(java.util.Map.of("ok", true));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(routeService.list());
    }
}
