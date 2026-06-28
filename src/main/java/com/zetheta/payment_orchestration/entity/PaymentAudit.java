package com.zetheta.payment_orchestration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAudit {

    @Id
    private UUID id;

    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "merchant_transaction_id")
    private String merchantTransactionId;

    private BigDecimal amount;

    private String currency;

    @Column(name = "payment_method")
    private String paymentMethod;

    private String gateway;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}