package com.IntelStream.application.workflow;


import com.IntelStream.infrastructure.persistence.entity.LoanApplication;
import com.IntelStream.infrastructure.persistence.repository.LoanApplicationRepository;
import com.IntelStream.presentation.dto.request.LoanRequest;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanApplicationOrchestrator {

    private final WorkflowClient workflowClient;
    private final LoanApplicationRepository repository;

    private static final String TASK_QUEUE = "LOAN_PROCESSING_QUEUE";

    public LoanApplication orchestrateLoanApplication(LoanRequest request) {
        String workflowId = "loan-" + UUID.randomUUID();

        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(workflowId)
                .setTaskQueue(TASK_QUEUE)
                .setWorkflowExecutionTimeout(Duration.ofMinutes(10))
                .setWorkflowRunTimeout(Duration.ofMinutes(5))
                .build();

        LoanApprovalWorkflow workflow = workflowClient.newWorkflowStub(LoanApprovalWorkflow.class, options);

        try {
            WorkflowClient.start(workflow::processLoanApplication, request);
        } catch (Exception e) {
            log.error("❌ Failed to start workflow", e);
            throw e;
        }

        log.info("🚀 Workflow started with ID: {}", workflowId);

        return waitForPersistedEntity(workflowId);
    }


    private LoanApplication waitForPersistedEntity(String workflowId) {
        try {
            Thread.sleep(1000); // simple delay; consider polling or callbacks for production
            return repository.findByWorkflowId(workflowId)
                    .orElseThrow(() -> new IllegalStateException("Application not found for workflowId: " + workflowId));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread interrupted while waiting for entity persistence", e);
        }
    }

    public String getWorkflowStatus(String workflowId) {
        try {
            return workflowClient
                    .newWorkflowStub(LoanApprovalWorkflow.class, workflowId)
                    .getCurrentStep();
        } catch (Exception e) {
            log.warn("⚠️ Failed to retrieve status for workflowId: {}", workflowId, e);
            return "UNKNOWN";
        }
    }

    public void cancelWorkflow(String workflowId, String reason) {
        try {
            workflowClient
                    .newWorkflowStub(LoanApprovalWorkflow.class, workflowId)
                    .requestCancellation(reason);
            log.info("🛑 Workflow {} cancellation requested", workflowId);
        } catch (Exception e) {
            log.error("❌ Failed to cancel workflow {}", workflowId, e);
        }
    }
}
