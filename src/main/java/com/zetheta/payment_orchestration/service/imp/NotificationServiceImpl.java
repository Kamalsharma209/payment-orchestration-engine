package com.zetheta.payment_orchestration.service.imp;

import com.zetheta.payment_orchestration.entity.Notification;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import com.zetheta.payment_orchestration.repository.NotificationRepository;
import com.zetheta.payment_orchestration.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotification(PaymentCreatedEvent event) {

        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .transactionId(event.getTransactionId())
                .merchantTransactionId(event.getMerchantTransactionId())
                .notificationType("EMAIL")
                .recipient("customer@example.com")
                .message(buildMessage(event))
                .status("SENT")
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        System.out.println("======================================");
        System.out.println("EMAIL SENT");
        System.out.println("To      : customer@example.com");
        System.out.println("Subject : Payment Notification");
        System.out.println(notification.getMessage());
        System.out.println("======================================");
    }

    private String buildMessage(PaymentCreatedEvent event) {

        if (event.getEventType() == null) {
            return "Payment update received.";
        }

        return switch (event.getEventType()) {

            case "PAYMENT_CREATED" ->
                    "Your payment has been created.";

            case "PAYMENT_AUTHORIZED" ->
                    "Your payment has been authorized.";

            case "PAYMENT_CAPTURED" ->
                    "Your payment has been captured successfully.";

            case "PAYMENT_FAILED" ->
                    "Your payment has failed.";

            case "PAYMENT_REFUNDED" ->
                    "Your refund has been processed successfully.";

            default ->
                    "Payment update received.";
        };
    }
}