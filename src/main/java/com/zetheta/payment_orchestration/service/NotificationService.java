package com.zetheta.payment_orchestration.service;

import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;

public interface NotificationService {

    void sendNotification(PaymentCreatedEvent event);

}