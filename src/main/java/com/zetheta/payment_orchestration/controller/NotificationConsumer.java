package com.zetheta.payment_orchestration.controller;

import com.zetheta.payment_orchestration.config.RabbitMQConfig;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import com.zetheta.payment_orchestration.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void consumeNotification(PaymentCreatedEvent event) {

        log.info("Notification Consumer received event: {}",
                event.getEventType());

        notificationService.sendNotification(event);
    }
}