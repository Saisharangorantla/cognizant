package com.acme.paymentservice.dto;

import java.math.BigDecimal;

public record PaymentRequest(String orderId, BigDecimal amount) {
}
