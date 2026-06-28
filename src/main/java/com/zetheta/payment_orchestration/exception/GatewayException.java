package com.zetheta.payment_orchestration.exception;

public class GatewayException extends RuntimeException {

    public GatewayException(String message) {
        super(message);
    }
}