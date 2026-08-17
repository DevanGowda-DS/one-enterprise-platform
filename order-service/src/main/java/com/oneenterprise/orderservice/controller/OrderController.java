package com.oneenterprise.orderservice.controller;

import com.oneenterprise.orderservice.dto.OrderResponse;
import com.oneenterprise.orderservice.service.OrderService;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable @Positive Long orderId) {
        return orderService.getOrder(orderId);
    }
}
