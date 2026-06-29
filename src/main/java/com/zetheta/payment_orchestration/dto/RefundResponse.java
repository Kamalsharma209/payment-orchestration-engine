package com.zetheta.payment_orchestration.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RefundResponse {

    private UUID refundId;

    private UUID transactionId;

    private String merchantTransactionId;

    private BigDecimal amount;

    private String reason;

    private String status;

    private LocalDateTime createdAt;
}