package com.acme.inventoryservice.dto;

public record InventoryResponse(Long id, Long productId, String productName, int stockLevel, boolean lowStock) {
}
