package com.acme.inventoryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * No hardcoded URL here - "product-service" is resolved through Eureka
 * (via spring-cloud-starter-netflix-eureka-client + the load-balanced
 * Feign integration that comes with spring-cloud-starter-openfeign),
 * so this keeps working even if product-service moves to a different
 * host/port or scales to multiple instances.
 */
@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductDto getProduct(@PathVariable("id") Long id);

    record ProductDto(Long id, String name, java.math.BigDecimal price, String currency) {
    }
}
