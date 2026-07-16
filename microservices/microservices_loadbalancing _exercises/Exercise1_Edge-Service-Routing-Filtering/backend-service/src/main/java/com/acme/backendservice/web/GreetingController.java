package com.acme.backendservice.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal downstream service used to demonstrate that the gateway is
 * actually routing traffic somewhere real, and not just echoing back
 * a canned response.
 */
@RestController
public class GreetingController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/example/hello")
    public Map<String, Object> hello() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Hello from backend-service");
        body.put("servedByPort", port);
        body.put("timestamp", Instant.now().toString());
        return body;
    }
}
