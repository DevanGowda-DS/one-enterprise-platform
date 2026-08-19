package com.oneenterprise.userservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DAY 7 NEW CLASS
 * Simple controller to VERIFY that config
 * is being loaded from Config Server.
 *
 * Call GET /config/message to see which
 * environment's config is active.
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    // DAY 7 NEW: reads from Config Server
    // Value comes from user-service-dev.properties
    // in config repo (not local application.properties!)
    @Value("${app.message:Config not loaded}")
    private String message;

    // DAY 7 NEW: reads environment name from Config Server
    @Value("${app.environment:UNKNOWN}")
    private String environment;

    /**
     * DAY 7 NEW ENDPOINT
     * Test endpoint to verify Config Server is working
     * GET http://localhost:8081/config/message
     */
    @GetMapping("/message")
    public String getMessage() {
        return "Message: " + message
                + " | Environment: " + environment;
    }

    /**
     * DAY 7 NEW ENDPOINT
     * Shows all config info
     * GET http://localhost:8081/config/info
     */
    @GetMapping("/info")
    public String getInfo() {
        return "Service: user-service"
                + " | Environment: " + environment
                + " | Message: " + message;
    }
}