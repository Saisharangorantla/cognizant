package com.acme.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryRequest(@NotNull Long productId, @Min(0) int stockLevel) {
}
