package com.zetheta.payment_orchestration.service.imp;

import com.zetheta.payment_orchestration.dto.CreatePaymentRequest;
import com.zetheta.payment_orchestration.dto.PaymentResponse;
import com.zetheta.payment_orchestration.entity.Transaction;
import com.zetheta.payment_orchestration.enums.TransactionState;
import com.zetheta.payment_orchestration.exception.DuplicateTransactionException;
import com.zetheta.payment_orchestration.repository.TransactionRepository;
import com.zetheta.payment_orchestration.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        // Check duplicate transaction (Idempotency)
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
                .status(transaction.getTransactionState().name())
                .message("Payment Created Successfully")
                .build();
    }
}