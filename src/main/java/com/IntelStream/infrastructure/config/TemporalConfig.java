package com.IntelStream.infrastructure.config;

import com.IntelStream.application.activity.LoanProcessingActivitiesImpl;
import com.IntelStream.application.workflow.LoanApprovalWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class TemporalConfig {

    private static final String TASK_QUEUE = "LOAN_PROCESSING_QUEUE";

    private final LoanProcessingActivitiesImpl activities;

    private WorkerFactory workerFactory;

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newLocalServiceStubs();
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs serviceStubs) {
        return WorkflowClient.newInstance(serviceStubs);
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        this.workerFactory = WorkerFactory.newInstance(workflowClient);
        return this.workerFactory;
    }

    @Bean
    public Worker loanProcessingWorker() {
        Worker worker = workerFactory.newWorker(TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(LoanApprovalWorkflowImpl.class);
        worker.registerActivitiesImplementations(activities);
        return worker;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startWorker() {
        log.info("🟡 Waiting for Temporal service to be available...");

        // Optional retry block (add sleep if needed)
        for (int i = 1; i <= 5; i++) {
            try {
                workerFactory.start();
                log.info("✅ Temporal worker factory started and listening to queue: {}", TASK_QUEUE);
                break;
            } catch (Exception e) {
                log.warn("❌ Attempt {} to start worker failed: {}", i, e.getMessage());
                try {
                    Thread.sleep(2000); // Wait before retry
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
