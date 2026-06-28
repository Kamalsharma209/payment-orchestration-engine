package com.zetheta.payment_orchestration.service.imp;

import com.zetheta.payment_orchestration.entity.PaymentAudit;
import com.zetheta.payment_orchestration.event.PaymentCreatedEvent;
import com.zetheta.payment_orchestration.repository.PaymentAuditRepository;
import com.zetheta.payment_orchestration.service.PaymentAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentAuditServiceImpl implements PaymentAuditService {

    private final PaymentAuditRepository paymentAuditRepository;

    @Override
    public void saveAudit(PaymentCreatedEvent event) {

        PaymentAudit paymentAudit = PaymentAudit.builder()
                .id(UUID.randomUUID())
                .transactionId(event.getTransactionId())
                .merchantTransactionId(event.getMerchantTransactionId())
                .amount(event.getAmount())
                .currency(event.getCurrency())
                .paymentMethod(event.getPaymentMethod())
                .gateway(event.getGateway())
                .eventType("PAYMENT_CREATED")
                .createdAt(event.getCreatedAt())
                .build();

        paymentAuditRepository.save(paymentAudit);

        System.out.println("====================================");
        System.out.println("Payment Audit Saved Successfully");
        System.out.println("====================================");
    }
}