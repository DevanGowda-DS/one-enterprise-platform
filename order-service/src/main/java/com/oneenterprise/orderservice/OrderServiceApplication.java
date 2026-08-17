package com.oneenterprise.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Order Service — owns Order data, consumes User Service over HTTP (Day 1 + Day 5).
 * Runs independently on port 8082 (see application.properties).
 */
@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
