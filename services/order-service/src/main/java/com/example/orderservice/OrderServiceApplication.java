package com.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@RestController
@RequestMapping("/api/orders")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    private final ConcurrentHashMap<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong(1000);

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        if (request.userId() == null || request.productId() == null
                || request.quantity() == null || request.quantity() < 1) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Valid userId, productId and quantity are required"
            );
        }

        long id = ids.incrementAndGet();
        Order order = new Order(id, request.userId(), request.productId(),
                                request.quantity(), "CREATED");
        orders.put(id, order);
        return order;
    }

    @GetMapping
    public List<Order> getOrders() {
        return orders.values().stream()
            .sorted((a, b) -> Long.compare(a.id(), b.id()))
            .toList();
    }

    public record CreateOrderRequest(
        Long userId, Long productId, Integer quantity
    ) {}

    public record Order(
        Long id, Long userId, Long productId, Integer quantity, String status
    ) {}
}
