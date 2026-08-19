package com.oneenterprise.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * DAY 7 NEW CLASS
 * Config Server - central configuration source
 * for all microservices in the platform.
 *
 * @EnableConfigServer - makes this app a Config Server
 * All services will read their config from here
 * instead of their own application.properties
 */
@SpringBootApplication
@EnableConfigServer  // DAY 7 NEW: this annotation makes it Config Server
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                ConfigServerApplication.class, args
        );
        System.out.println("=================================");
        System.out.println("Config Server started at :8888");
        System.out.println("Test: http://localhost:8888/user-service/dev");
        System.out.println("=================================");
    }
}