package com.zetheta.payment_orchestration.service.imp;

import com.zetheta.payment_orchestration.dto.RefundRequest;
import com.zetheta.payment_orchestration.dto.RefundResponse;
import com.zetheta.payment_orchestration.entity.Refund;
import com.zetheta.payment_orchestration.entity.Transaction;
import com.zetheta.payment_orchestration.enums.TransactionState;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import com.zetheta.payment_orchestration.exception.RefundException;
import com.zetheta.payment_orchestration.producer.PaymentEventProducer;
import com.zetheta.payment_orchestration.repository.RefundRepository;
import com.zetheta.payment_orchestration.repository.TransactionRepository;
import com.zetheta.payment_orchestration.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentEventProducer paymentEventProducer;

    @Override
    public RefundResponse refundPayment(
            UUID transactionId,
            RefundRequest request) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new RefundException("Transaction not found."));

        // Only captured payments can be refunded
        if (transaction.getTransactionState() != TransactionState.CAPTURED) {
            throw new RefundException(
                    "Only CAPTURED payments can be refunded.");
        }

        // Prevent duplicate refund request
        refundRepository.findByTransactionIdAndReason(
                        transaction.getId(),
                        request.getReason())
                .ifPresent(existingRefund -> {
                    throw new RefundException(
                            "Refund request already exists.");
                });

        // Calculate already refunded amount
        BigDecimal refundedAmount = refundRepository.findByTransactionId(transactionId)
                .stream()
                .map(Refund::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Remaining refundable amount
        BigDecimal remaining =
                transaction.getAmount().subtract(refundedAmount);

        // Prevent over refund
        if (request.getAmount().compareTo(remaining) > 0) {
            throw new RefundException(
                    "Refund amount exceeds remaining refundable amount.");
        }

        // Create refund
        Refund refund = Refund.builder()
                .id(UUID.randomUUID())
                .transactionId(transaction.getId())
                .merchantTransactionId(transaction.getMerchantTransactionId())
                .amount(request.getAmount())
                .reason(request.getReason())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();

        refundRepository.save(refund);

        // Check if payment is fully refunded
        BigDecimal totalRefunded =
                refundedAmount.add(request.getAmount());

        if (totalRefunded.compareTo(transaction.getAmount()) == 0) {

            transaction.setTransactionState(TransactionState.REFUNDED);
            transaction.setUpdatedAt(LocalDateTime.now());

            transactionRepository.save(transaction);
        }

        // Publish refund event
        PaymentCreatedEvent event = PaymentCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .transactionId(transaction.getId())
                .merchantTransactionId(transaction.getMerchantTransactionId())
                .amount(refund.getAmount())
                .currency(transaction.getCurrency())
                .paymentMethod(transaction.getPaymentMethod().name())
                .gateway(transaction.getGateway() == null
                        ? null
                        : transaction.getGateway().name())
                .eventType("PAYMENT_REFUNDED")
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        paymentEventProducer.publishPaymentCreatedEvent(event);

        return RefundResponse.builder()
                .refundId(refund.getId())
                .transactionId(transaction.getId())
                .merchantTransactionId(transaction.getMerchantTransactionId())
                .amount(refund.getAmount())
                .reason(refund.getReason())
                .status(refund.getStatus())
                .createdAt(refund.getCreatedAt())
                .build();
    }
}