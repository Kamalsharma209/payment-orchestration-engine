package com.zetheta.payment_orchestration.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
public class RabbitMQConfig {

    // Queue Name
    public static final String PAYMENT_QUEUE = "payment.queue";

    // Exchange Name
    public static final String PAYMENT_EXCHANGE = "payment.exchange";

    // Routing Key
    public static final String PAYMENT_ROUTING_KEY = "payment.routing.key";

    @Bean
    public Queue paymentQueue() {
        System.out.println("Creating Queue Bean");
        return new Queue(PAYMENT_QUEUE, true);
    }

    @Bean
    public DirectExchange paymentExchange() {
        System.out.println("Creating Exchange Bean");
        return new DirectExchange(PAYMENT_EXCHANGE, true, false);
    }

    @Bean
    public Binding paymentBinding(
            Queue paymentQueue,
            DirectExchange paymentExchange) {

        System.out.println("Creating Binding Bean");

        return BindingBuilder
                .bind(paymentQueue)
                .to(paymentExchange)
                .with(PAYMENT_ROUTING_KEY);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {

        System.out.println("Creating RabbitAdmin");

        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

}