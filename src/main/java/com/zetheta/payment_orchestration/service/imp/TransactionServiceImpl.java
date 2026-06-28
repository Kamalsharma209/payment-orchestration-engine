package com.zetheta.payment_orchestration.service.imp;
import com.zetheta.payment_orchestration.dto.UpdateTransactionStateRequest;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import com.zetheta.payment_orchestration.exception.GatewayException;
import com.zetheta.payment_orchestration.exception.InvalidTransactionStateException;
import com.zetheta.payment_orchestration.dto.CreatePaymentRequest;
import com.zetheta.payment_orchestration.dto.PaymentResponse;
import com.zetheta.payment_orchestration.entity.Transaction;
import com.zetheta.payment_orchestration.enums.TransactionState;
import com.zetheta.payment_orchestration.exception.DuplicateTransactionException;
import com.zetheta.payment_orchestration.gateway.GatewayFactory;
import com.zetheta.payment_orchestration.gateway.GatewayResponse;
import com.zetheta.payment_orchestration.gateway.PaymentGatewayStrategy;
import com.zetheta.payment_orchestration.producer.PaymentEventProducer;
import com.zetheta.payment_orchestration.repository.TransactionRepository;
import com.zetheta.payment_orchestration.routing.GatewayRoutingService;
import com.zetheta.payment_orchestration.service.TransactionService;
import com.zetheta.payment_orchestration.util.RetryExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final GatewayRoutingService gatewayRoutingService;
    private final GatewayFactory gatewayFactory;
    private final RetryExecutor retryExecutor;
    private final PaymentEventProducer paymentEventProducer;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        if (transactionRepository.findByMerchantTransactionId(
                request.getMerchantTransactionId()).isPresent()) {

            throw new DuplicateTransactionException(
                    "Transaction already exists with merchantTransactionId: "
                            + request.getMerchantTransactionId());
        }

        Transaction transaction = Transaction.builder()
                .merchantTransactionId(request.getMerchantTransactionId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .paymentMethod(request.getPaymentMethod())
                .transactionState(TransactionState.CREATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        String selectedGateway = gatewayRoutingService.chooseGateway(transaction);

        GatewayResponse response;

        try {

            PaymentGatewayStrategy gateway =
                    gatewayFactory.getGateway(selectedGateway);
            response = retryExecutor.execute(gateway, transaction);

        } catch (GatewayException ex) {
            gatewayRoutingService.markGatewayFailure(selectedGateway);

            System.out.println("Primary gateway failed: " + ex.getMessage());

            String backupGateway =
                    gatewayRoutingService.getBackupGateway(selectedGateway);

            System.out.println("Switching to backup gateway: " + backupGateway);

            PaymentGatewayStrategy backup =
                    gatewayFactory.getGateway(backupGateway);
            response = retryExecutor.execute(backup, transaction);

            selectedGateway = backupGateway;
        }


        // Step 4: Update transaction
        if (response.isSuccess()) {
            gatewayRoutingService.markGatewaySuccess(selectedGateway);

            transaction.setGateway(
                    com.zetheta.payment_orchestration.enums.PaymentGateway.valueOf(selectedGateway));

            transaction.setTransactionState(TransactionState.ROUTE_SELECTED);
        } else {
            transaction.setTransactionState(TransactionState.FAILED);
        }

        transaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);
        PaymentCreatedEvent event = PaymentCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .transactionId(transaction.getId())
                .merchantTransactionId(transaction.getMerchantTransactionId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .paymentMethod(transaction.getPaymentMethod().name())
                .gateway(
                        transaction.getGateway() == null
                                ? null
                                : transaction.getGateway().name()
                )
                .eventType("PAYMENT_CREATED")
                .createdAt(transaction.getCreatedAt())
                .build();

        paymentEventProducer.publishPaymentCreatedEvent(event);

        return PaymentResponse.builder()
                .transactionId(transaction.getId())
                .merchantTransactionId(transaction.getMerchantTransactionId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .paymentMethod(transaction.getPaymentMethod().name())
                .gateway(transaction.getGateway() == null
                        ? null
                        : transaction.getGateway().name())
                .transactionState(transaction.getTransactionState().name())
                .message(response.getMessage())
                .build();
    }

    @Override
    public PaymentResponse getPayment(UUID transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));

        return PaymentResponse.builder()
                .transactionId(transaction.getId())
                .merchantTransactionId(transaction.getMerchantTransactionId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .paymentMethod(transaction.getPaymentMethod().name())
                .gateway(transaction.getGateway() == null
                        ? null
                        : transaction.getGateway().name())
                .transactionState(transaction.getTransactionState().name())
                .message("Transaction fetched successfully")
                .build();
    }

    @Override
    public PaymentResponse updateTransactionState(
            UUID transactionId,
            UpdateTransactionStateRequest request) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));

        TransactionState currentState = transaction.getTransactionState();
        TransactionState newState = request.getTransactionState();

        if (!isValidTransition(currentState, newState)) {
            throw new InvalidTransactionStateException(
                    "Cannot move transaction from "
                            + currentState
                            + " to "
                            + newState);
        }

        transaction.setTransactionState(newState);
        transaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        return PaymentResponse.builder()
                .transactionId(transaction.getId())
                .merchantTransactionId(transaction.getMerchantTransactionId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .paymentMethod(transaction.getPaymentMethod().name())
                .gateway(transaction.getGateway() == null
                        ? null
                        : transaction.getGateway().name())
                .transactionState(transaction.getTransactionState().name())
                .message("Transaction state updated successfully")
                .build();
    }
    private boolean isValidTransition(TransactionState current,
                                      TransactionState next) {

        return switch (current) {

            case CREATED ->
                    next == TransactionState.ROUTE_SELECTED
                            || next == TransactionState.FAILED;

            case ROUTE_SELECTED ->
                    next == TransactionState.AUTH_INITIATED
                            || next == TransactionState.FAILED;

            case AUTH_INITIATED ->
                    next == TransactionState.AUTHORIZED
                            || next == TransactionState.FAILED;

            case AUTHORIZED ->
                    next == TransactionState.CAPTURED
                            || next == TransactionState.FAILED;

            case CAPTURED ->
                    next == TransactionState.REFUNDED;

            default -> false;
        };
    }
}