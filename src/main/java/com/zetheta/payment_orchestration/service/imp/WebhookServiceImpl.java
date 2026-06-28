package com.zetheta.payment_orchestration.service.imp;

import com.zetheta.payment_orchestration.dto.PaymentWebhookRequest;
import com.zetheta.payment_orchestration.entity.Transaction;
import com.zetheta.payment_orchestration.enums.PaymentGateway;
import com.zetheta.payment_orchestration.repository.TransactionRepository;
import com.zetheta.payment_orchestration.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import com.zetheta.payment_orchestration.producer.PaymentEventProducer;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

    private final TransactionRepository transactionRepository;
    private final PaymentEventProducer paymentEventProducer;

    @Override
    public void processWebhook(PaymentWebhookRequest request) {

        Transaction transaction = transactionRepository
                .findByMerchantTransactionId(request.getMerchantTransactionId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Transaction not found with merchantTransactionId: "
                                        + request.getMerchantTransactionId()));

        transaction.setTransactionState(request.getTransactionState());

        if (request.getGateway() != null) {
            transaction.setGateway(
                    PaymentGateway.valueOf(request.getGateway().toUpperCase())
            );
        }

        transaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);
        PaymentCreatedEvent event = PaymentCreatedEvent.builder()
                .transactionId(transaction.getId())
                .merchantTransactionId(transaction.getMerchantTransactionId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .paymentMethod(transaction.getPaymentMethod().name())
                .gateway(transaction.getGateway().name())
                .eventType("PAYMENT_" + transaction.getTransactionState().name())
                .createdAt(transaction.getUpdatedAt())
                .build();

        paymentEventProducer.publishPaymentCreatedEvent(event);

        System.out.println("=======================================");
        System.out.println("Webhook Processed Successfully");
        System.out.println("Merchant Transaction ID : "
                + transaction.getMerchantTransactionId());
        System.out.println("New State : "
                + transaction.getTransactionState());
        System.out.println("=======================================");
    }
}