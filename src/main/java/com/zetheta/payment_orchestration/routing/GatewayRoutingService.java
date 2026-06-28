package com.zetheta.payment_orchestration.routing;

import com.zetheta.payment_orchestration.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatewayRoutingService implements RoutingStrategy {

    private final List<GatewayMetrics> metrics = new java.util.ArrayList<>(
            java.util.List.of(

                    new GatewayMetrics("RAZORPAY",98,300,2,true,0),

                    new GatewayMetrics("STRIPE",95,350,3,true,0),

                    new GatewayMetrics("PAYU",85,700,1,true,0),

                    new GatewayMetrics("UPI",99,120,0,true,0)
            )
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
    public String getBackupGateway(String failedGateway) {

        return metrics.stream()
                .filter(GatewayMetrics::isHealthy)
                .map(GatewayMetrics::getGatewayName)
                .filter(name -> !name.equalsIgnoreCase(failedGateway))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No backup gateway available"));
    }
    public void markGatewayFailure(String gatewayName){

        metrics.stream()

                .filter(g->g.getGatewayName().equalsIgnoreCase(gatewayName))

                .findFirst()

                .ifPresent(gateway->{

                    gateway.setFailureCount(
                            gateway.getFailureCount()+1);

                    if(gateway.getFailureCount()>=2){

                        gateway.setHealthy(false);

                        System.out.println(
                                gatewayName+
                                        " marked as UNHEALTHY");
                    }

                });

    }
    public void markGatewaySuccess(String gatewayName){

        metrics.stream()

                .filter(g->g.getGatewayName().equalsIgnoreCase(gatewayName))

                .findFirst()

                .ifPresent(gateway->{

                    gateway.setFailureCount(0);

                    gateway.setHealthy(true);

                });

    }

}