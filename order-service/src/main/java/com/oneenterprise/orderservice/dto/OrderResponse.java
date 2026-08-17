package com.oneenterprise.orderservice.dto;

/**
 * Public API contract for Order Service (Day 2: intentional response model).
 */
public record OrderResponse(
        Long orderId,
        Long userId,
        String userName,
        String userEmail,
        String status
) {
    public static OrderResponse fulfilled(Long orderId, Long userId, UserResponse user) {
        return new OrderResponse(orderId, userId, user.name(), user.email(), "CONFIRMED");
    }

    public static OrderResponse degraded(Long orderId, Long userId, String reason) {
        return new OrderResponse(orderId, userId, null, null, "PENDING_USER_INFO: " + reason);
    }
}
