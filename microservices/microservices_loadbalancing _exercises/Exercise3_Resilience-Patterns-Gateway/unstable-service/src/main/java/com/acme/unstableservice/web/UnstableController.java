package com.acme.unstableservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Deliberately flaky endpoint used to exercise the gateway's circuit
 * breaker. Roughly 40% of calls fail immediately, another 15% hang long
 * enough to trip the gateway's time limiter, and the rest succeed
 * normally. Call it enough times through the gateway and you should see
 * the breaker trip open and start returning the fallback response
 * without even bothering to call this service.
 */
@RestController
public class UnstableController {

    @GetMapping("/unstable/hello")
    public Map<String, Object> hello() throws InterruptedException {
        double roll = ThreadLocalRandom.current().nextDouble();

        if (roll < 0.40) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Simulated downstream failure");
        }
        if (roll < 0.55) {
            // Sleep longer than the gateway's configured timeLimiter duration (3s)
            // so this call also counts as a failure from the circuit breaker's point of view.
            Thread.sleep(4000);
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Hello from unstable-service");
        body.put("status", "ok");
        return body;
    }
}
