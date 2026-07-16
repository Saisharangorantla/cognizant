package com.acme.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderRequest(
        @NotNull Long userId,
        @NotBlank String productName,
        @Min(1) int quantity,
        @NotNull @Positive BigDecimal totalAmount
) {
}
