package com.zetheta.payment_orchestration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private UUID transactionId;

    private String merchantTransactionId;

    private BigDecimal amount;

    private String currency;

    private String paymentMethod;

    private String gateway;

    private String transactionState;

    private String message;
}