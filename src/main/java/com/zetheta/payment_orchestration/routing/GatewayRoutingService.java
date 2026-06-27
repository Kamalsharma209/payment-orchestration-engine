package com.zetheta.payment_orchestration.routing;

import com.zetheta.payment_orchestration.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatewayRoutingService implements RoutingStrategy {

    private final List<GatewayMetrics> metrics = List.of(

            new GatewayMetrics("RAZORPAY",98,300,2,true),

            new GatewayMetrics("STRIPE",95,350,3,true),

            new GatewayMetrics("PAYU",85,700,1,true),

            new GatewayMetrics("UPI",99,120,0,true)
    );

    @Override
    public String chooseGateway(Transaction transaction) {

        return metrics.stream()

                .filter(GatewayMetrics::isHealthy)

                .sorted((a,b)->{

                    if(a.getSuccessRate()!=b.getSuccessRate()){

                        return Double.compare(
                                b.getSuccessRate(),
                                a.getSuccessRate());
                    }

                    return Long.compare(
                            a.getAverageResponseTime(),
                            b.getAverageResponseTime());

                })

                .findFirst()

                .orElseThrow(()->
                        new RuntimeException("No healthy gateway found"))
                .getGatewayName();
    }

}