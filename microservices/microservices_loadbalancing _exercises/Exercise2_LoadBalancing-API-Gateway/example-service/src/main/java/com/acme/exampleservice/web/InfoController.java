package com.acme.exampleservice.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Returns which port answered the request, so that when the gateway
 * distributes calls across two running instances of this service we can
 * actually see the load balancer alternating between them.
 */
@RestController
public class InfoController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/loadbalanced/hello")
    public Map<String, Object> hello() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Hello from example-service");
        body.put("handledByInstanceOnPort", port);
        return body;
    }
}
