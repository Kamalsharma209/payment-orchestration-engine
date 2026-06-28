package com.zetheta.payment_orchestration.service.imp;

import com.zetheta.payment_orchestration.entity.ProcessedEvent;
import com.zetheta.payment_orchestration.repository.ProcessedEventRepository;
import com.zetheta.payment_orchestration.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdempotencyServiceImpl implements IdempotencyService {

    private final ProcessedEventRepository processedEventRepository;

    @Override
    public boolean isProcessed(String eventId, String consumerName) {

        return processedEventRepository
                .findByEventIdAndConsumerName(eventId, consumerName)
                .isPresent();
    }

    @Override
    public void markProcessed(
            String eventId,
            String consumerName,
            String eventType) {

        ProcessedEvent processedEvent = ProcessedEvent.builder()
                .id(UUID.randomUUID())
                .eventId(eventId)
                .consumerName(consumerName)
                .eventType(eventType)
                .processedAt(LocalDateTime.now())
                .build();

        processedEventRepository.save(processedEvent);
    }
}