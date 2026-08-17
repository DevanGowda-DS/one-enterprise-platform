package com.oneenterprise.orderservice.model;

/**
 * Internal order representation. Order Service owns this data exclusively (Day 3).
 */
public class Order {

    private final Long orderId;
    private final Long userId;
    private final String item;

    public Order(Long orderId, Long userId, String item) {
        this.orderId = orderId;
        this.userId = userId;
        this.item = item;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getItem() {
        return item;
    }
}
