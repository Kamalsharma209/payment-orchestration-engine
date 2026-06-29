package com.zetheta.payment_orchestration.service;

import com.zetheta.payment_orchestration.dto.RefundRequest;
import com.zetheta.payment_orchestration.dto.RefundResponse;

import java.util.UUID;

public interface RefundService {

    RefundResponse refundPayment(
            UUID transactionId,
            RefundRequest request);
}