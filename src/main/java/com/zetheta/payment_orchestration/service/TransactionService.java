package com.zetheta.payment_orchestration.service;

import com.zetheta.payment_orchestration.dto.CreatePaymentRequest;
import com.zetheta.payment_orchestration.dto.PaymentResponse;
import com.zetheta.payment_orchestration.dto.UpdateTransactionStateRequest;

import java.util.UUID;

public interface TransactionService {

    /**
     * Create a new payment transaction.
     */
    PaymentResponse createPayment(CreatePaymentRequest request);

    /**
     * Fetch payment details using transaction ID.
     */
    PaymentResponse getPayment(UUID transactionId);

    /**
     * Update transaction state.
     */
    PaymentResponse updateTransactionState(
            UUID transactionId,
            UpdateTransactionStateRequest request
    );
}