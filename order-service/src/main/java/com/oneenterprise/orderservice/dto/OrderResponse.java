package com.oneenterprise.orderservice.dto;


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
