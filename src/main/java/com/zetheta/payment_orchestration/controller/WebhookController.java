package com.zetheta.payment_orchestration.controller;

import com.zetheta.payment_orchestration.dto.PaymentWebhookRequest;
import com.zetheta.payment_orchestration.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/payment")
    public ResponseEntity<String> paymentWebhook(
            @RequestBody PaymentWebhookRequest request) {

        webhookService.processWebhook(request);

        return ResponseEntity.ok("Webhook processed successfully");
    }
}