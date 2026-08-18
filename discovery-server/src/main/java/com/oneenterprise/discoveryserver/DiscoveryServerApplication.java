package com.oneenterprise.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Day 6: Eureka Service Registry.
 *
 *  User Service and Order
 * Service each register themselves here on startup (under their
 * spring.application.name), and the Gateway queries this registry to
 * resolve a logical service name (e.g. USER-SERVICE) to a live instance
 * address instead of relying on a hard-coded host/port.
 *
 * this entire module is new. Runs on port 8761.
 * Dashboard: http://localhost:8761
 *
 */
@SpringBootApplication
@EnableEurekaServer // Day 6: turns this plain Spring Boot app into a Eureka registry
public class DiscoveryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
}
