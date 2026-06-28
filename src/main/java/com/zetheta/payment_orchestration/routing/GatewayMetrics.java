package com.zetheta.payment_orchestration.routing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayMetrics {

    private String gatewayName;

    private double successRate;

    private long averageResponseTime;

    private double transactionCost;

    private boolean healthy;

    private int failureCount;
}