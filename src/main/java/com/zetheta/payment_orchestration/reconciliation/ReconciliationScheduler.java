package com.zetheta.payment_orchestration.reconciliation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReconciliationScheduler {

    private final ReconciliationService reconciliationService;

    @Scheduled(fixedRate = 60000)
    public void reconcile() {

        log.info("Running Scheduled Reconciliation...");

        reconciliationService.reconcileTransactions();
    }
}