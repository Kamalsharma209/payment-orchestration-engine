package com.zetheta.payment_orchestration.repository;

import com.zetheta.payment_orchestration.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProcessedEventRepository
        extends JpaRepository<ProcessedEvent, UUID> {

    Optional<ProcessedEvent> findByEventIdAndConsumerName(
            String eventId,
            String consumerName
    );
}