package com.zetheta.payment_orchestration.controller;

import com.zetheta.payment_orchestration.dto.RefundRequest;
import com.zetheta.payment_orchestration.dto.RefundResponse;
import com.zetheta.payment_orchestration.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping("/{transactionId}/refund")
    public ResponseEntity<RefundResponse> refundPayment(
            @PathVariable UUID transactionId,
            @Valid @RequestBody RefundRequest request) {

        return ResponseEntity.ok(
                refundService.refundPayment(transactionId, request));
    }
}