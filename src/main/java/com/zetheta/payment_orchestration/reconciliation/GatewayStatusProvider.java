package com.zetheta.payment_orchestration.reconciliation;

import com.zetheta.payment_orchestration.enums.TransactionState;

public interface GatewayStatusProvider {

    TransactionState getTransactionStatus(String merchantTransactionId);

}