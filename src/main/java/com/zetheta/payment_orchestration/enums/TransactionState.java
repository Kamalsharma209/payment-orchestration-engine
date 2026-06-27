package com.zetheta.payment_orchestration.enums;

public enum TransactionState {

    CREATED,

    ROUTE_SELECTED,

    AUTH_INITIATED,

    AUTHORIZED,

    CAPTURED,

    FAILED,

    REFUNDED

}