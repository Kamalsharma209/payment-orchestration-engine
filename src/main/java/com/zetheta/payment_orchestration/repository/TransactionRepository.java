package com.zetheta.payment_orchestration.repository;

import com.zetheta.payment_orchestration.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByMerchantTransactionId(
            String merchantTransactionId);

}