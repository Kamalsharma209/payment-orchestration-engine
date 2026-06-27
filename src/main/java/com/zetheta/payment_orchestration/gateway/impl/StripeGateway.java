package com.zetheta.payment_orchestration.gateway.impl;

import com.zetheta.payment_orchestration.entity.Transaction;
import com.zetheta.payment_orchestration.gateway.GatewayResponse;
import com.zetheta.payment_orchestration.gateway.PaymentGatewayStrategy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StripeGateway implements PaymentGatewayStrategy {

    @Override
    public String getGatewayName() {
        return "STRIPE";
    }

    @Override
    public GatewayResponse processPayment(Transaction transaction) {

        return GatewayResponse.builder()
                .success(true)
                .gatewayTransactionId("STR-" + UUID.randomUUID())
                .message("Payment processed successfully by Razorpay")
                .build();
    }
}