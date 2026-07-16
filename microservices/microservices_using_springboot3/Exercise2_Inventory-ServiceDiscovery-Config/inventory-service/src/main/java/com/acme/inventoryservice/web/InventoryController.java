package com.acme.inventoryservice.web;

import com.acme.inventoryservice.client.ProductClient;
import com.acme.inventoryservice.dto.InventoryRequest;
import com.acme.inventoryservice.dto.InventoryResponse;
import com.acme.inventoryservice.entity.InventoryItem;
import com.acme.inventoryservice.repository.InventoryItemRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryItemRepository inventoryItemRepository;
    private final ProductClient productClient;

    // Pulled from config-server (see config-repo/inventory-service.yml).
    @Value("${inventory.low-stock-threshold:5}")
    private int lowStockThreshold;

    public InventoryController(InventoryItemRepository inventoryItemRepository, ProductClient productClient) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.productClient = productClient;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> create(@Valid @RequestBody InventoryRequest request) {
        // Cross-service, Eureka-discovered call: make sure the product actually exists.
        ProductClient.ProductDto product = productClient.getProduct(request.productId());

        InventoryItem saved = inventoryItemRepository.save(
                new InventoryItem(product.id(), request.stockLevel()));

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved, product.name()));
    }

    @GetMapping
    public List<InventoryResponse> findAll() {
        return inventoryItemRepository.findAll().stream()
                .map(item -> toResponse(item, resolveProductNameSafely(item.getProductId())))
                .toList();
    }

    @PutMapping("/{productId}/stock")
    public InventoryResponse adjustStock(@PathVariable Long productId, @Valid @RequestBody InventoryRequest request) {
        InventoryItem item = inventoryItemRepository.findByProductId(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No inventory record for product " + productId));

        item.setStockLevel(request.stockLevel());
        InventoryItem saved = inventoryItemRepository.save(item);
        return toResponse(saved, resolveProductNameSafely(productId));
    }

    private String resolveProductNameSafely(Long productId) {
        try {
            return productClient.getProduct(productId).name();
        } catch (Exception ex) {
            return "unknown (product-service unavailable)";
        }
    }

    private InventoryResponse toResponse(InventoryItem item, String productName) {
        return new InventoryResponse(item.getId(), item.getProductId(), productName,
                item.getStockLevel(), item.getStockLevel() < lowStockThreshold);
    }
}
