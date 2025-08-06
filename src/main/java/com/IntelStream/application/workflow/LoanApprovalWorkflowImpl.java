package com.intelstream.application.workflow.impl;

import com.IntelStream.application.activity.LoanProcessingActivities;
import com.IntelStream.application.workflow.LoanApprovalWorkflow;
import com.IntelStream.domain.model.ActivityResult;
import com.IntelStream.infrastructure.persistence.enums.LoanStatus;
import com.IntelStream.infrastructure.persistence.enums.OrchestrationStep;
import com.IntelStream.presentation.dto.request.LoanRequest;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.workflow.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Component
@Slf4j
public class LoanApprovalWorkflowImpl implements LoanApprovalWorkflow {

    private String currentStep = "INITIALIZED";
    private LoanStatus currentStatus = LoanStatus.PENDING;
    private boolean cancelled = false;
    private String cancellationReason;

    private final LoanProcessingActivities activities = Workflow.newActivityStub(
            LoanProcessingActivities.class,
            ActivityOptions.newBuilder()
                    .setScheduleToCloseTimeout(Duration.ofMinutes(5))
                    .setStartToCloseTimeout(Duration.ofMinutes(2))
                    .setRetryOptions(
                            RetryOptions.newBuilder()
                                    .setInitialInterval(Duration.ofSeconds(1))
                                    .setMaximumInterval(Duration.ofSeconds(30))
                                    .setBackoffCoefficient(2.0)
                                    .setMaximumAttempts(3)
                                    .build()
                    ).build()
    );

    @Override
    public ActivityResult processLoanApplication(LoanRequest request) {

        final String workflowId = Workflow.getInfo().getWorkflowId();
        final String runId = Workflow.getInfo().getRunId();

        final String applicationId = persistApplication(request, workflowId, runId);

        final List<WorkflowStep> steps = Arrays.asList(
                WorkflowStep.of("CREDIT_CHECK", OrchestrationStep.CREDIT_CHECK, () -> activities.performCreditCheck(request)),
                WorkflowStep.of("DOCUMENT_VERIFICATION", OrchestrationStep.DOCUMENT_VERIFICATION, () -> activities.verifyDocuments(request)),
                WorkflowStep.of("RISK_ASSESSMENT", OrchestrationStep.RISK_ASSESSMENT, () -> activities.assessRisk(request)),
                WorkflowStep.of("LOAN_APPROVAL", OrchestrationStep.LOAN_APPROVAL, () -> activities.approveLoan(request))
        );

        return runSteps(applicationId, request.getEmail(), steps);
    }

    private String persistApplication(LoanRequest request, String workflowId, String runId) {
        ActivityResult result = executeStep("PERSIST_APPLICATION", () ->
                activities.persistApplication(request, workflowId, runId)
        );

        if (!result.isSuccess()) {
            throw ApplicationFailure.newNonRetryableFailure("APPLICATION_PERSIST_FAILED", result.getMessage());
        }

        return (String) result.getData();
    }

    private ActivityResult runSteps(String applicationId, String email, List<WorkflowStep> steps) {
        for (WorkflowStep step : steps) {

            // Cancelled early?
            if (cancelled) {
                return buildCancelledResult(step.name);
            }

            currentStep = step.name;
            currentStatus = LoanStatus.PROCESSING;

            activities.updateApplicationStatus(applicationId, LoanStatus.PROCESSING, step.enumStep);

            ActivityResult result = step.execute();

            if (!result.isSuccess()) {
                return compensateAndReject(applicationId, step, result.getMessage(), email);
            }

            log.info("✅ Step {} completed successfully", step.name);
        }

        finalizeSuccess(applicationId, email);
        return ActivityResult.builder()
                .success(true)
                .message("Loan application approved successfully")
                .stepName("COMPLETED")
                .data(applicationId)
                .build();
    }

    private ActivityResult compensateAndReject(String applicationId, WorkflowStep failedStep, String reason, String email) {
        log.warn("⚠️ Compensation triggered for step {} due to: {}", failedStep.name, reason);

        currentStep = "COMPENSATING";
        currentStatus = LoanStatus.COMPENSATING;

        activities.rejectLoan(applicationId, reason);
        activities.updateApplicationStatus(applicationId, LoanStatus.REJECTED, OrchestrationStep.FAILED);
        activities.sendNotification(applicationId, "Loan application rejected: " + reason, email);

        currentStep = "COMPENSATED";
        currentStatus = LoanStatus.REJECTED;

        return ActivityResult.builder()
                .success(false)
                .message("Loan application rejected after compensation")
                .stepName("COMPENSATED")
                .errorCode("STEP_FAILED")
                .data(applicationId)
                .build();
    }

    private void finalizeSuccess(String applicationId, String email) {
        currentStep = "COMPLETED";
        currentStatus = LoanStatus.APPROVED;

        activities.updateApplicationStatus(applicationId, LoanStatus.APPROVED, OrchestrationStep.COMPLETED);
        activities.sendNotification(applicationId, "Loan approved successfully!", email);

        log.info("✅ Workflow COMPLETED for application {}", applicationId);
    }

    private ActivityResult executeStep(String stepName, Supplier<ActivityResult> stepExecutor) {
        currentStep = stepName;
        return stepExecutor.get();
    }

    private ActivityResult buildCancelledResult(String stepName) {
        log.warn("🛑 Workflow cancelled before step {}. Reason: {}", stepName, cancellationReason);

        return ActivityResult.builder()
                .success(false)
                .message("Workflow cancelled: " + cancellationReason)
                .stepName(stepName)
                .errorCode("CANCELLED")
                .build();
    }

    @Override
    public void requestCancellation(String reason) {
        log.warn("🚨 Cancellation requested: {}", reason);
        this.cancelled = true;
        this.cancellationReason = reason;
    }

    @Override
    public String getCurrentStep() {
        return currentStep;
    }

    @Override
    public LoanStatus getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Internal static class to represent each workflow step
     */
    private static class WorkflowStep {
        private final String name;
        private final OrchestrationStep enumStep;
        private final Supplier<ActivityResult> activitySupplier;

        private WorkflowStep(String name, OrchestrationStep enumStep, Supplier<ActivityResult> activitySupplier) {
            this.name = name;
            this.enumStep = enumStep;
            this.activitySupplier = activitySupplier;
        }

        public static WorkflowStep of(String name, OrchestrationStep step, Supplier<ActivityResult> supplier) {
            return new WorkflowStep(name, step, supplier);
        }

        public ActivityResult execute() {
            return activitySupplier.get();
        }
    }
}
