package com.acme.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Declarative HTTP client for user-service. The "name" is only used for
 * Feign's internal client naming/metrics here since we're calling a
 * fixed URL (no service discovery in this exercise) - see
 * user-service.url in application.yml.
 */
@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserDto getUser(@PathVariable("id") Long id);

    record UserDto(Long id, String fullName, String email) {
    }
}
