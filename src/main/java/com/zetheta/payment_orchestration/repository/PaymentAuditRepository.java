package com.zetheta.payment_orchestration.repository;

import com.zetheta.payment_orchestration.entity.PaymentAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentAuditRepository
        extends JpaRepository<PaymentAudit, UUID> {
}