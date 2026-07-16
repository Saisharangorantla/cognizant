package com.acme.billingservice.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Same idea as customer-service: this only ever answers under
 * "/billing", the gateway owns the public-facing "/api/billing" prefix.
 */
@RestController
public class BillingController {

    @GetMapping("/billing")
    public List<Map<String, Object>> list() {
        return List.of(invoice(101, "42.50"));
    }

    @GetMapping("/billing/{invoiceId}")
    public Map<String, Object> byId(@PathVariable int invoiceId) {
        return invoice(invoiceId, "42.50");
    }

    private Map<String, Object> invoice(int id, String amount) {
        Map<String, Object> invoice = new LinkedHashMap<>();
        invoice.put("invoiceId", id);
        invoice.put("amountDue", new BigDecimal(amount));
        invoice.put("servedAt", Instant.now().toString());
        return invoice;
    }
}
