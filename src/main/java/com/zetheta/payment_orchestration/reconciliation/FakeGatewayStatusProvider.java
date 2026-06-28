package com.zetheta.payment_orchestration.reconciliation;

import com.zetheta.payment_orchestration.enums.TransactionState;
import org.springframework.stereotype.Component;

@Component
public class FakeGatewayStatusProvider implements GatewayStatusProvider {

    @Override
    public TransactionState getTransactionStatus(String merchantTransactionId) {

        if (merchantTransactionId.endsWith("1")) {
            return TransactionState.CAPTURED;
        }

        if (merchantTransactionId.endsWith("2")) {
            return TransactionState.FAILED;
        }

        return TransactionState.AUTHORIZED;
    }
}