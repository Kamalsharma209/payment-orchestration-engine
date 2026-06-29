package com.zetheta.payment_orchestration.exception;

public class RefundException extends RuntimeException {

    public RefundException(String message) {
        super(message);
    }
}