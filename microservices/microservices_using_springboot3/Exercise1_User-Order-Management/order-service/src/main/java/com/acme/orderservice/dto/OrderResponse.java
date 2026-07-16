package com.acme.orderservice.dto;

import java.math.BigDecimal;

public record OrderResponse(
        Long id,
        Long userId,
        String customerName,
        String productName,
        int quantity,
        BigDecimal totalAmount
) {
}
