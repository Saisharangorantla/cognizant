package com.acme.resilientgateway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Invoked by the gateway's CircuitBreaker filter whenever the call to
 * unstable-service fails or times out, or while the breaker is open.
 * Returning a clear, well-formed response here beats letting the caller
 * see a raw connection error or a hung request.
 */
@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public ResponseEntity<Map<String, Object>> fallback() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "fallback");
        body.put("message", "unstable-service is currently unavailable. Please try again shortly.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }
}
