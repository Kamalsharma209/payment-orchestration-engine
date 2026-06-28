package com.zetheta.payment_orchestration.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitTestConfig {

    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void testConnection() {
        System.out.println("=================================");
        System.out.println("RabbitMQ Connected Successfully");
        System.out.println("=================================");
    }
}