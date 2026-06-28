package com.zetheta.payment_orchestration.consumer;

import com.zetheta.payment_orchestration.config.RabbitMQConfig;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import com.zetheta.payment_orchestration.producer.PaymentEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Slf4j
@Component
public class DeadLetterConsumer {

    private final PaymentEventProducer paymentEventProducer;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_NOTIFICATION_DLQ)
    public void consumeDeadLetter(PaymentCreatedEvent event) {

        if (event.getRetryCount() >= 3) {

            log.error("Maximum retry reached for {}",
                    event.getMerchantTransactionId());

            return;
        }

        event.setRetryCount(event.getRetryCount() + 1);

        log.info("Retry Attempt : {}", event.getRetryCount());

        paymentEventProducer.publishPaymentCreatedEvent(event);
    }
}