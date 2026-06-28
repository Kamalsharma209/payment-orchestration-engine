package com.zetheta.payment_orchestration.consumer;

import com.zetheta.payment_orchestration.config.RabbitMQConfig;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import com.zetheta.payment_orchestration.service.PaymentAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final PaymentAuditService paymentAuditService;
    @RabbitListener(queues = RabbitMQConfig.PAYMENT_AUDIT_QUEUE)
    public void consumePaymentCreatedEvent(PaymentCreatedEvent event) {

        log.info("========================================");
        log.info("Payment Event Received");
        log.info("Transaction ID : {}", event.getTransactionId());
        log.info("Merchant Txn ID: {}", event.getMerchantTransactionId());
        log.info("Amount         : {}", event.getAmount());
        log.info("Gateway        : {}", event.getGateway());
        log.info("========================================");

        paymentAuditService.saveAudit(event);
    }
}