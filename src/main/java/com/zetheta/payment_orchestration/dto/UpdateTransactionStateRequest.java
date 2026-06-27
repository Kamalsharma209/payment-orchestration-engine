package com.zetheta.payment_orchestration.dto;

import com.zetheta.payment_orchestration.enums.TransactionState;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTransactionStateRequest {

    @NotNull(message = "Transaction state is required")
    private TransactionState transactionState;

}