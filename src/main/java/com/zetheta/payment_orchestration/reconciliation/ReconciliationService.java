package com.zetheta.payment_orchestration.reconciliation;

import com.zetheta.payment_orchestration.entity.Transaction;
import com.zetheta.payment_orchestration.enums.TransactionState;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import com.zetheta.payment_orchestration.producer.PaymentEventProducer;
import com.zetheta.payment_orchestration.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReconciliationService {

    private final TransactionRepository transactionRepository;
    private final GatewayStatusProvider gatewayStatusProvider;
    private final PaymentEventProducer paymentEventProducer;

    public void reconcileTransactions() {

        List<Transaction> transactions =
                transactionRepository.findByTransactionStateIn(
                        List.of(
                                TransactionState.AUTH_INITIATED,
                                TransactionState.AUTHORIZED,
                                TransactionState.ROUTE_SELECTED
                        ));

        log.info("Starting reconciliation for {} transactions", transactions.size());

        for (Transaction transaction : transactions) {
            reconcile(transaction);
        }

        log.info("Reconciliation completed");
    }

    private void reconcile(Transaction transaction) {

        TransactionState gatewayState =
                gatewayStatusProvider.getTransactionStatus(
                        transaction.getMerchantTransactionId());

        if (transaction.getTransactionState() == gatewayState) {
            return;
        }

        log.warn("==============================");
        log.warn("RECONCILIATION MISMATCH");
        log.warn("Merchant Txn : {}", transaction.getMerchantTransactionId());
        log.warn("Database     : {}", transaction.getTransactionState());
        log.warn("Gateway      : {}", gatewayState);

        transaction.setTransactionState(gatewayState);
        transaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        PaymentCreatedEvent event = PaymentCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .transactionId(transaction.getId())
                .merchantTransactionId(transaction.getMerchantTransactionId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .paymentMethod(transaction.getPaymentMethod().name())
                .gateway(transaction.getGateway() != null
                        ? transaction.getGateway().name()
                        : null)
                .eventType("PAYMENT_RECONCILED")
                .createdAt(LocalDateTime.now())
                .retryCount(0)
                .build();

        paymentEventProducer.publishPaymentCreatedEvent(event);

        log.info("==============================");
        log.info("RECONCILIATION COMPLETED");
        log.info("Merchant Txn : {}", transaction.getMerchantTransactionId());
        log.info("New State    : {}", gatewayState);
        log.info("==============================");
    }
}

