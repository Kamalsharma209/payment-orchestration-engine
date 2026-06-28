package com.zetheta.payment_orchestration.consumer;

import com.zetheta.payment_orchestration.config.RabbitMQConfig;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void consumePaymentCreatedEvent(PaymentCreatedEvent event) {

        log.info("========================================");
        log.info("Payment Event Received");
        log.info("Transaction ID : {}", event.getTransactionId());
        log.info("Merchant Txn ID: {}", event.getMerchantTransactionId());
        log.info("Amount         : {}", event.getAmount());
        log.info("Gateway        : {}", event.getGateway());
        log.info("========================================");
    }
}