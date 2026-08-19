package com.oneenterprise.orderservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DAY 7 NEW CLASS
 * Verifies order-service reads config from Config Server
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    // DAY 7 NEW: from order-service-dev.properties in config repo
    @Value("${app.message:Config not loaded}")
    private String message;

    @Value("${app.environment:UNKNOWN}")
    private String environment;

    /**
     * DAY 7 NEW ENDPOINT
     * GET http://localhost:8082/config/message
     */
    @GetMapping("/message")
    public String getMessage() {
        return "Message: " + message
                + " | Environment: " + environment;
    }
}