package com.zetheta.payment_orchestration.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

    // Exchange
    public static final String PAYMENT_EXCHANGE = "payment.exchange";

    // Routing Key
    public static final String PAYMENT_ROUTING_KEY = "payment.routing.key";

    // Queues
    public static final String PAYMENT_AUDIT_QUEUE = "payment.audit.queue";

    public static final String PAYMENT_NOTIFICATION_QUEUE = "payment.notification.queue";

    @Bean
    public Queue paymentAuditQueue() {
        return new Queue(PAYMENT_AUDIT_QUEUE, true);
    }

    @Bean
    public Queue paymentNotificationQueue() {

        return QueueBuilder
                .durable(PAYMENT_NOTIFICATION_QUEUE)
                .deadLetterExchange(PAYMENT_DLX)
                .deadLetterRoutingKey(PAYMENT_DLX_ROUTING_KEY)
                .build();
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(PAYMENT_EXCHANGE, true, false);
    }

    @Bean
    public Binding auditBinding(
            Queue paymentAuditQueue,
            DirectExchange paymentExchange) {

        return BindingBuilder
                .bind(paymentAuditQueue)
                .to(paymentExchange)
                .with(PAYMENT_ROUTING_KEY);
    }

    @Bean
    public Binding notificationBinding(
            Queue paymentNotificationQueue,
            DirectExchange paymentExchange) {

        return BindingBuilder
                .bind(paymentNotificationQueue)
                .to(paymentExchange)
                .with(PAYMENT_ROUTING_KEY);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {

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
    public static final String PAYMENT_DLX =
            "payment.dlx.exchange";

    public static final String PAYMENT_NOTIFICATION_DLQ =
            "payment.notification.dlq";

    public static final String PAYMENT_DLX_ROUTING_KEY =
            "payment.dlx.routing.key";
    @Bean
    public DirectExchange paymentDeadLetterExchange() {
        return new DirectExchange(PAYMENT_DLX, true, false);
    }
    @Bean
    public Queue paymentNotificationDLQ() {
        return new Queue(PAYMENT_NOTIFICATION_DLQ, true);
    }
    @Bean
    public Binding deadLetterBinding(
            Queue paymentNotificationDLQ,
            DirectExchange paymentDeadLetterExchange) {

        return BindingBuilder
                .bind(paymentNotificationDLQ)
                .to(paymentDeadLetterExchange)
                .with(PAYMENT_DLX_ROUTING_KEY);
    }
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);

        // Important
        factory.setDefaultRequeueRejected(false);

        return factory;
    }
}