package com.zetheta.payment_orchestration.controller;

import com.zetheta.payment_orchestration.dto.CreatePaymentRequest;
import com.zetheta.payment_orchestration.dto.PaymentResponse;
import com.zetheta.payment_orchestration.dto.UpdateTransactionStateRequest;
import com.zetheta.payment_orchestration.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createPayment(request));
    }
    @GetMapping("/{transactionId}")
    public ResponseEntity<PaymentResponse> getPayment(
            @PathVariable UUID transactionId) {

        return ResponseEntity.ok(
                transactionService.getPayment(transactionId)
        );
    }
    @PutMapping("/{transactionId}/state")
    public ResponseEntity<PaymentResponse> updateTransactionState(
            @PathVariable UUID transactionId,
            @Valid @RequestBody UpdateTransactionStateRequest request) {

        return ResponseEntity.ok(
                transactionService.updateTransactionState(transactionId, request)
        );
    }
}