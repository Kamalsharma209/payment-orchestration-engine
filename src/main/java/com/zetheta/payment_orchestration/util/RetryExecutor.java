package com.zetheta.payment_orchestration.util;

import com.zetheta.payment_orchestration.exception.GatewayException;
import com.zetheta.payment_orchestration.gateway.GatewayResponse;
import com.zetheta.payment_orchestration.gateway.PaymentGatewayStrategy;
import com.zetheta.payment_orchestration.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class RetryExecutor {

    private static final int MAX_RETRIES = 2;

    public GatewayResponse execute(PaymentGatewayStrategy gateway,
                                   Transaction transaction) {

        GatewayException lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {

            try {

                System.out.println("Attempt " + attempt +
                        " using " + gateway.getGatewayName());

                return gateway.processPayment(transaction);

            } catch (GatewayException ex) {

                lastException = ex;

                System.out.println("Attempt "
                        + attempt
                        + " failed.");

            }
        }

        throw lastException;
    }
}