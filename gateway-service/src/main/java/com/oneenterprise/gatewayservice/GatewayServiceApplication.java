package com.oneenterprise.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway (Day 3) — single entry point for clients.
 * Routes /api/users/** -> User Service, /api/orders/** -> Order Service.
 * Runs on port 8080.
 */
@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}
