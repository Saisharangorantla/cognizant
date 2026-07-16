package com.acme.paymentservice.service;

import com.acme.paymentservice.client.ThirdPartyAuthorizationClient;
import com.acme.paymentservice.dto.PaymentRequest;
import com.acme.paymentservice.dto.PaymentResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Wraps the call to the (slow, occasionally-down) third-party API with a
 * circuit breaker and a time limiter, both named "paymentService" -
 * matching the instance name configured in application.yml.
 * <p>
 * @TimeLimiter requires an async return type, so the actual blocking
 * HTTP call is pushed onto the common ForkJoinPool via
 * CompletableFuture.supplyAsync - that's what lets Resilience4j
 * cancel/time out a call that's taking too long, something you can't do
 * to a plain synchronous method call.
 */
@Service
public class ThirdPartyPaymentGateway {

    private static final Logger log = LoggerFactory.getLogger(ThirdPartyPaymentGateway.class);

    private final ThirdPartyAuthorizationClient client;

    public ThirdPartyPaymentGateway(ThirdPartyAuthorizationClient client) {
        this.client = client;
    }

    @CircuitBreaker(name = "paymentService", fallbackMethod = "fallback")
    @TimeLimiter(name = "paymentService")
    public CompletableFuture<PaymentResult> charge(PaymentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> response = client.authorize(request);
            String authorizationCode = String.valueOf(response.get("authorizationCode"));
            return new PaymentResult("APPROVED", "Authorized with code " + authorizationCode);
        });
    }

    /**
     * Signature must match charge(...) plus a trailing Throwable -
     * Resilience4j calls this whenever the circuit is open, the call
     * times out, or the underlying call throws.
     */
    private CompletableFuture<PaymentResult> fallback(PaymentRequest request, Throwable throwable) {
        log.warn("Falling back for order {} (amount {}) - reason: {}",
                request.orderId(), request.amount(), throwable.toString());

        return CompletableFuture.completedFuture(
                new PaymentResult("PENDING", "Payment provider is currently unavailable; your payment will be retried automatically."));
    }
}
