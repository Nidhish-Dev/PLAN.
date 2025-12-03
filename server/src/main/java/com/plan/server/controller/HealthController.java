package com.plan.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple health-check endpoint
 * Useful for load balancers, Kubernetes liveness/readiness probes, monitoring tools, etc.
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @Value("${spring.application.name:server}")
    private String appName;

    @Value("${server.port:8080}")
    private String port;

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "UP");
        body.put("service", appName);
        body.put("port", port);
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("message", "PLAN server is healthy!");

        return ResponseEntity.ok(body);
    }

    // Optional: lightweight ping endpoint (very fast, no body)
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}