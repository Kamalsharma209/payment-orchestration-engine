package com.zetheta.payment_orchestration.service;

import com.zetheta.payment_orchestration.dto.CreatePaymentRequest;
import com.zetheta.payment_orchestration.dto.PaymentResponse;
import com.zetheta.payment_orchestration.dto.UpdateTransactionStateRequest;

import java.util.UUID;

public interface TransactionService {

    /**
     * Creates a new payment transaction.
     *
     *  Payment request details
     * @return Payment response containing transaction details
     */
    PaymentResponse getPayment(UUID transactionId);

    PaymentResponse updateTransactionState(
            UUID transactionId,
            UpdateTransactionStateRequest request);

}