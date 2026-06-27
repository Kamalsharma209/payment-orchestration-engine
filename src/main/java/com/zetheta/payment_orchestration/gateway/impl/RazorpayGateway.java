package com.zetheta.payment_orchestration.gateway.impl;

import com.zetheta.payment_orchestration.entity.Transaction;
import com.zetheta.payment_orchestration.gateway.GatewayResponse;
import com.zetheta.payment_orchestration.gateway.PaymentGatewayStrategy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RazorpayGateway implements PaymentGatewayStrategy {

    @Override
    public String getGatewayName() {
        return "RAZORPAY";
    }

    @Override
    public GatewayResponse processPayment(Transaction transaction) {

        return GatewayResponse.builder()
                .success(true)
                .gatewayTransactionId("RZP-" + UUID.randomUUID())
                .message("Payment processed successfully by Razorpay")
                .build();
    }
}