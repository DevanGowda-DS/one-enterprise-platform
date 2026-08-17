package com.oneenterprise.orderservice.service;

import com.oneenterprise.orderservice.client.UserClient;
import com.oneenterprise.orderservice.dto.OrderResponse;
import com.oneenterprise.orderservice.dto.UserResponse;
import com.oneenterprise.orderservice.exception.OrderNotFoundException;
import com.oneenterprise.orderservice.exception.UserServiceUnavailableException;
import com.oneenterprise.orderservice.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Order Service owns order data and enriches it with user data obtained
 * from User Service over HTTP (Day 1, Day 3, Day 5).
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final UserClient userClient;

    public OrderService(UserClient userClient) {
        this.userClient = userClient;
        orders.put(5001L, new Order(5001L, 1001L, "Mechanical Keyboard"));
        orders.put(5002L, new Order(5002L, 1002L, "Standing Desk"));
        orders.put(5003L, new Order(5003L, 1003L, "Noise Cancelling Headphones"));
    }

    public OrderResponse getOrder(Long orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        // Day 2 lesson: distinguish "order not found" from "downstream user-service failure".
        try {
            UserResponse user = userClient.getUser(order.getUserId());
            return OrderResponse.fulfilled(order.getOrderId(), order.getUserId(), user);
        } catch (UserServiceUnavailableException ex) {
            log.warn("Returning degraded order response for orderId={} because: {}", orderId, ex.getMessage());
            return OrderResponse.degraded(order.getOrderId(), order.getUserId(), ex.getMessage());
        }
    }
}
