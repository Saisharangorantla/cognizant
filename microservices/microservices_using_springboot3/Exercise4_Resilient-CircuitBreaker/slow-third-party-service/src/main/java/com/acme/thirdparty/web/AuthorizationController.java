package com.acme.thirdparty.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Pretends to be a third-party payment authorization API: sometimes
 * slow, sometimes down, mostly fine. Deliberately unpredictable so
 * payment-service's circuit breaker has something real to react to.
 */
@RestController
public class AuthorizationController {

    @PostMapping("/external/authorize")
    public Map<String, Object> authorize() throws InterruptedException {
        double outcome = ThreadLocalRandom.current().nextDouble();

        if (outcome < 0.30) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Upstream processor rejected the request");
        }
        if (outcome < 0.50) {
            // Long enough to blow past payment-service's 2s timeout.
            Thread.sleep(3500);
        } else {
            // A little natural latency even on the "fine" path.
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 600));
        }

        return Map.of(
                "authorizationCode", "AUTH-" + ThreadLocalRandom.current().nextInt(100000, 999999),
                "status", "APPROVED"
        );
    }
}
