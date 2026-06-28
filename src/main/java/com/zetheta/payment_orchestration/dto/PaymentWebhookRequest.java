package com.zetheta.payment_orchestration.dto;

import com.zetheta.payment_orchestration.enums.TransactionState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentWebhookRequest {

    private String merchantTransactionId;

    private TransactionState transactionState;

    private String gateway;

    private String signature;

}