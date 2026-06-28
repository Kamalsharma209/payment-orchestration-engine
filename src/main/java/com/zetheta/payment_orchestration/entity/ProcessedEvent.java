package com.zetheta.payment_orchestration.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "processed_events",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_consumer",
                        columnNames = {"event_id", "consumer_name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedEvent {

    @Id
    private UUID id;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "consumer_name", nullable = false)
    private String consumerName;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;
}