package com.zetheta.payment_orchestration.producer;

import com.zetheta.payment_orchestration.config.RabbitMQConfig;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishPaymentCreatedEvent(PaymentCreatedEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                RabbitMQConfig.PAYMENT_ROUTING_KEY,
                event
        );

        System.out.println("====================================");
        System.out.println("Payment Event Published Successfully");
        System.out.println(event);
        System.out.println("====================================");
    }
}