package com.zetheta.payment_orchestration.dto;

import com.zetheta.payment_orchestration.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {

    @NotBlank
    private String merchantTransactionId;

    @NotNull
    @DecimalMin("1.00")
    private BigDecimal amount;

    @NotBlank
    private String currency;

    @NotNull
    private PaymentMethod paymentMethod;
}