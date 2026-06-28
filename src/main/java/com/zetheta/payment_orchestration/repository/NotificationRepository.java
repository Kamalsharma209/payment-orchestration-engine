package com.zetheta.payment_orchestration.repository;

import com.zetheta.payment_orchestration.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<Notification, UUID> {

}