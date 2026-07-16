package com.acme.customerservice.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Note the path here is "/customers", not "/api/customers" - the
 * gateway is responsible for rewriting the public "/api/customers/**"
 * path down to this one, so this service doesn't need to know or care
 * what prefix the gateway exposes it under.
 */
@RestController
public class CustomerController {

    @GetMapping("/customers")
    public List<Map<String, Object>> list() {
        Map<String, Object> customer = new LinkedHashMap<>();
        customer.put("id", 1);
        customer.put("name", "Grace Hopper");
        customer.put("servedAt", Instant.now().toString());
        return List.of(customer);
    }

    @GetMapping("/customers/{id}")
    public Map<String, Object> byId(@PathVariable int id) {
        Map<String, Object> customer = new LinkedHashMap<>();
        customer.put("id", id);
        customer.put("name", "Grace Hopper");
        customer.put("servedAt", Instant.now().toString());
        return customer;
    }
}
