package com.zetheta.payment_orchestration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PaymentResponse {

    private UUID transactionId;

    private String status;

    private String message;
}