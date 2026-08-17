package com.oneenterprise.orderservice.dto;

/**
 * Day 5 lesson: this is Order Service's OWN representation of what User Service returns.
 * Order Service does not share code/classes with User Service — each service defines
 * its own contract for data it consumes, keeping the services independently deployable.
 */
public record UserResponse(
        Long id,
        String name,
        String email
) {
}
