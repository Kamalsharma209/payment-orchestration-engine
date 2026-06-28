package com.zetheta.payment_orchestration.service;

public interface IdempotencyService {

    boolean isProcessed(
            String eventId,
            String consumerName
    );

    void markProcessed(
            String eventId,
            String consumerName,
            String eventType
    );

}