package com.zetheta.payment_orchestration.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GatewayFactory {

    private final List<PaymentGateway> paymentGateways;

    public PaymentGateway getGateway(String gatewayName) {

        return paymentGateways.stream()
                .filter(g -> g.getGatewayName().equalsIgnoreCase(gatewayName))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Gateway not found: " + gatewayName));
    }
}