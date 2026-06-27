package com.zetheta.payment_orchestration.gateway;

import com.zetheta.payment_orchestration.entity.Transaction;

public interface PaymentGatewayStrategy {

    String getGatewayName();

    GatewayResponse processPayment(Transaction transaction);

}