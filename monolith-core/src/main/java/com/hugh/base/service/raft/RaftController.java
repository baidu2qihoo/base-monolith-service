package com.hugh.base.service.raft;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Very small Raft stub for node registration & heartbeats. For production replace with real Raft lib.
 */
@RestController
@RequestMapping("/api/raft")
public class RaftController {

    private static final ConcurrentHashMap<String, NodeInfo> nodes = new ConcurrentHashMap<>();

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody NodeInfo info) {
        info.setLastHeartbeat(Instant.now().toString());
        nodes.put(info.getNodeId(), info);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<?> heartbeat(@RequestBody Map<String,String> body) {
        String id = body.get("nodeId");
        NodeInfo n = nodes.get(id);
        if (n != null) {
            n.setLastHeartbeat(Instant.now().toString());
            return ResponseEntity.ok(Map.of("ok", true));
        }
        return ResponseEntity.status(404).body(Map.of("ok", false));
    }

    @GetMapping("/members")
    public ResponseEntity<?> members() {
        return ResponseEntity.ok(nodes.values());
    }
}
