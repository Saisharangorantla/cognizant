package com.acme.paymentservice.web;

import com.acme.paymentservice.dto.PaymentRequest;
import com.acme.paymentservice.dto.PaymentResult;
import com.acme.paymentservice.service.ThirdPartyPaymentGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class PaymentController {

    private final ThirdPartyPaymentGateway paymentGateway;

    public PaymentController(ThirdPartyPaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    /**
     * Spring MVC supports CompletableFuture return types natively - the
     * request is handled asynchronously and the response is written once
     * the future (from ThirdPartyPaymentGateway.charge, or its fallback)
     * completes.
     */
    @PostMapping("/payments/charge")
    public CompletableFuture<PaymentResult> charge(@RequestBody PaymentRequest request) {
        return paymentGateway.charge(request);
    }
}
