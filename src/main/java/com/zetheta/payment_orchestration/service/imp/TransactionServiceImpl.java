package com.zetheta.payment_orchestration.service.imp;
import com.zetheta.payment_orchestration.dto.UpdateTransactionStateRequest;
import com.zetheta.payment_orchestration.exception.InvalidTransactionStateException;
import com.zetheta.payment_orchestration.dto.CreatePaymentRequest;
import com.zetheta.payment_orchestration.dto.PaymentResponse;
import com.zetheta.payment_orchestration.dto.UpdateTransactionStateRequest;
import com.zetheta.payment_orchestration.entity.Transaction;
import com.zetheta.payment_orchestration.enums.TransactionState;
import com.zetheta.payment_orchestration.exception.DuplicateTransactionException;
import com.zetheta.payment_orchestration.repository.TransactionRepository;
import com.zetheta.payment_orchestration.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

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
                .message("Payment Created Successfully")
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