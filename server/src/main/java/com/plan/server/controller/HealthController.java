package com.plan.server.controller;

import com.plan.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Value("${spring.application.name:server}")
    private String appName;

    @Value("${server.port:8080}")
    private String port;

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @Autowired(required = false)
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "UP");
        body.put("service", appName);
        body.put("port", port);
        body.put("timestamp", LocalDateTime.now().toString());
        
        // Check MongoDB connection
        try {
            if (mongoTemplate != null) {
                String dbName = mongoTemplate.getDb().getName();
                body.put("mongodb", Map.of(
                    "status", "connected",
                    "database", dbName
                ));
                if (userRepository != null) {
                    long userCount = userRepository.count();
                    body.put("users_count", userCount);
                }
            } else {
                body.put("mongodb", Map.of("status", "not_configured"));
            }
        } catch (Exception e) {
            body.put("mongodb", Map.of(
                "status", "error",
                "error", e.getMessage()
            ));
        }
        
        body.put("message", "PLAN server is healthy!");

        return ResponseEntity.ok(body);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}