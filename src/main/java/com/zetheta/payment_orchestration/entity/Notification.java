package com.zetheta.payment_orchestration.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    private UUID id;

    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "merchant_transaction_id")
    private String merchantTransactionId;

    @Column(name = "notification_type")
    private String notificationType;

    private String recipient;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}