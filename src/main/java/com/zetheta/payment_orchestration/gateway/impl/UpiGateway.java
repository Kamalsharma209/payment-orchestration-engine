package com.zetheta.payment_orchestration.gateway.impl;

import com.zetheta.payment_orchestration.entity.Transaction;
import com.zetheta.payment_orchestration.gateway.GatewayResponse;
import com.zetheta.payment_orchestration.gateway.PaymentGateway;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UpiGateway implements PaymentGateway {

    @Override
    public String getGatewayName() {
        return "UPI";
    }

    @Override
    public GatewayResponse processPayment(Transaction transaction) {

        return GatewayResponse.builder()
                .success(true)
                .gatewayTransactionId("UPI-" + UUID.randomUUID())
                .message("Payment processed successfully by Razorpay")
                .build();
    }
}