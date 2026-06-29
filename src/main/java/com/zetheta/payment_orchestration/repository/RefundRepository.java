package com.zetheta.payment_orchestration.repository;

import com.zetheta.payment_orchestration.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefundRepository
        extends JpaRepository<Refund, UUID> {

    List<Refund> findByTransactionId(UUID transactionId);

    Optional<Refund> findByTransactionIdAndReason(
            UUID transactionId,
            String reason);

}