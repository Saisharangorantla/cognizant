package com.acme.orderservice.web;

import com.acme.orderservice.client.UserClient;
import com.acme.orderservice.dto.OrderRequest;
import com.acme.orderservice.dto.OrderResponse;
import com.acme.orderservice.entity.Order;
import com.acme.orderservice.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserClient userClient;

    public OrderController(OrderRepository orderRepository, UserClient userClient) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        // Cross-service call: confirm the user actually exists before we place an order for them.
        UserClient.UserDto user = userClient.getUser(request.userId());

        Order saved = orderRepository.save(new Order(
                user.id(), request.productName(), request.quantity(), request.totalAmount()));

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved, user.fullName()));
    }

    @GetMapping
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(order -> toResponse(order, resolveCustomerNameSafely(order.getUserId())))
                .toList();
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable Long id) {
        Order order = requireOrder(id);
        return toResponse(order, resolveCustomerNameSafely(order.getUserId()));
    }

    @GetMapping("/by-user/{userId}")
    public List<OrderResponse> findByUser(@PathVariable Long userId) {
        String customerName = resolveCustomerNameSafely(userId);
        return orderRepository.findByUserId(userId).stream()
                .map(order -> toResponse(order, customerName))
                .toList();
    }

    private Order requireOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No order with id " + id));
    }

    /**
     * Listing orders shouldn't fail outright just because user-service
     * happens to be down at that moment - we degrade gracefully instead.
     */
    private String resolveCustomerNameSafely(Long userId) {
        try {
            return userClient.getUser(userId).fullName();
        } catch (Exception ex) {
            return "unknown (user-service unavailable)";
        }
    }

    private static OrderResponse toResponse(Order order, String customerName) {
        return new OrderResponse(order.getId(), order.getUserId(), customerName,
                order.getProductName(), order.getQuantity(), order.getTotalAmount());
    }
}
