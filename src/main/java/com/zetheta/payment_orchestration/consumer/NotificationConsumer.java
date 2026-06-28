package com.zetheta.payment_orchestration.consumer;

import com.zetheta.payment_orchestration.config.RabbitMQConfig;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import com.zetheta.payment_orchestration.service.IdempotencyService;
import com.zetheta.payment_orchestration.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private static final String CONSUMER = "NOTIFICATION";

    private final NotificationService notificationService;
    private final IdempotencyService idempotencyService;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_NOTIFICATION_QUEUE)
    @RabbitListener(queues = RabbitMQConfig.PAYMENT_NOTIFICATION_QUEUE)
    public void consumeNotification(PaymentCreatedEvent event) {

        log.info("Notification received: {}", event.getMerchantTransactionId());

        throw new RuntimeException("Simulated Notification Failure");
    }
}