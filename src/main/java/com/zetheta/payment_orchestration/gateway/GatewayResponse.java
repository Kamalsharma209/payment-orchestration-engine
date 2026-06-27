package com.zetheta.payment_orchestration.gateway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayResponse {

    private boolean success;

    private String gatewayTransactionId;

    private String message;
}