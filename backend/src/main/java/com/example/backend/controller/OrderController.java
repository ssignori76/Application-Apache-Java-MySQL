package com.example.backend.controller;

import com.example.backend.dto.OrderDto;
import com.example.backend.dto.OrderRequest;
import com.example.backend.model.AppOrder;
import com.example.backend.model.User;
import com.example.backend.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        logger.info("User {} submitting new order for serviceId {}", user.getUsername(), request.getServiceId());
        AppOrder order = orderService.createOrder(user, request);
        return ResponseEntity.status(201).body(new OrderDto(order));
    }

    @GetMapping
    public List<OrderDto> getOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        logger.debug("Fetching orders for user {}", user.getUsername());
        return orderService.getCustomerOrders(user.getId())
                .stream().map(OrderDto::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        AppOrder order = orderService.getOrderByIdAndUser(id, user);
        return ResponseEntity.ok(new OrderDto(order));
    }
}
