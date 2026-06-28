package com.zetheta.payment_orchestration.service;

import com.zetheta.payment_orchestration.dto.PaymentWebhookRequest;

public interface WebhookService {

    void processWebhook(PaymentWebhookRequest request);

}