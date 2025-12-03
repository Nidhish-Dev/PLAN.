package com.plan.server.service;

import com.plan.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class HealthCheckService {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${spring.application.name:server}")
    private String appName;

    @Value("${server.port:8080}")
    private String port;

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @Autowired(required = false)
    private UserRepository userRepository;

    @Scheduled(fixedRate = 600000) // 10 minutes = 600,000 milliseconds
    public void performHealthCheck() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(formatter);
        
        logger.info("=== Health Check Started at {} ===", timestamp);
        logger.info("Service: {}", appName);
        logger.info("Port: {}", port);
        
        // Check MongoDB connection
        try {
            if (mongoTemplate != null) {
                String dbName = mongoTemplate.getDb().getName();
                logger.info("MongoDB Status: CONNECTED");
                logger.info("MongoDB Database: {}", dbName);
                
                if (userRepository != null) {
                    long userCount = userRepository.count();
                    logger.info("Users Count: {}", userCount);
                } else {
                    logger.warn("UserRepository not available");
                }
            } else {
                logger.error("MongoDB Status: NOT CONFIGURED - MongoTemplate is null");
            }
        } catch (Exception e) {
            logger.error("MongoDB Status: ERROR - {}", e.getMessage());
            logger.error("Exception details: ", e);
        }
        
        logger.info("=== Health Check Completed at {} ===", timestamp);
        logger.info(""); // Empty line for readability
    }
}

