package com.zetheta.payment_orchestration.service;

import com.zetheta.payment_orchestration.dto.CreatePaymentRequest;
import com.zetheta.payment_orchestration.dto.PaymentResponse;

public interface TransactionService {

    /**
     * Creates a new payment transaction.
     *
     * @param request Payment request details
     * @return Payment response containing transaction details
     */
    PaymentResponse createPayment(CreatePaymentRequest request);

}