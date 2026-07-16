package com.acme.paymentservice.client;

import com.acme.paymentservice.dto.PaymentRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * The raw, un-protected HTTP call to the third-party API. Deliberately
 * kept dumb - all the resilience behaviour (circuit breaking, timeouts,
 * fallback) lives one layer up in ThirdPartyPaymentGateway, so this
 * class stays a simple, easily-testable HTTP adapter.
 */
@Component
public class ThirdPartyAuthorizationClient {

    private final RestClient restClient;

    public ThirdPartyAuthorizationClient(RestClient thirdPartyRestClient) {
        this.restClient = thirdPartyRestClient;
    }

    public Map<String, Object> authorize(PaymentRequest request) {
        return restClient.post()
                .uri("/external/authorize")
                .body(request)
                .retrieve()
                .body(Map.class);
    }
}
