package com.zetheta.payment_orchestration.routing;

import com.zetheta.payment_orchestration.entity.Transaction;

public interface RoutingStrategy {

    String chooseGateway(Transaction transaction);

}