package com.IntelStream.application.activity;


import com.IntelStream.domain.model.ActivityResult;
import com.IntelStream.infrastructure.persistence.entity.LoanApplication;
import com.IntelStream.infrastructure.persistence.enums.LoanStatus;
import com.IntelStream.infrastructure.persistence.enums.OrchestrationStep;
import com.IntelStream.infrastructure.persistence.repository.LoanApplicationRepository;
import com.IntelStream.presentation.dto.request.LoanRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoanProcessingActivitiesImpl implements LoanProcessingActivities {

    private final LoanApplicationRepository repo;

    @Override
    @Transactional
    public ActivityResult persistApplication(LoanRequest request, String workflowId, String runId) {
        return safeExec("persistApplication", () -> {
            LoanApplication entity = LoanApplication.builder()
                    .applicantName(request.getApplicantName())
                    .email(request.getEmail())
                    .amount(request.getAmount())
                    .purpose(request.getPurpose())
                    .ssn(request.getSsn())
                    .annualIncome(request.getAnnualIncome())
                    .status(LoanStatus.PENDING)
                    .currentStep(OrchestrationStep.CREDIT_CHECK)
                    .workflowId(workflowId)
                    .runId(runId)
                    .build();
            LoanApplication saved = repo.save(entity);
            return success(saved.getApplicationId());
        });
    }

    @Override
    @Transactional
    public ActivityResult updateApplicationStatus(String applicationId, LoanStatus status, OrchestrationStep step) {
        return safeExec("updateApplicationStatus", () -> {
            return repo.findById(applicationId)
                    .map(app -> {
                        app.setStatus(status);
                        app.setCurrentStep(step);
                        repo.save(app);
                        return success(applicationId);
                    })
                    .orElse(failure("App not found: " + applicationId));
        });
    }

    @Override
    @Transactional
    public ActivityResult performCreditCheck(LoanRequest request) {
        return safeExec("performCreditCheck", () ->
                // simulate logic:
                request.getAnnualIncome().doubleValue() >= 30000
                        ? success("Credit OK")
                        : failure("Credit score too low")
        );
    }

    @Override
    @Transactional
    public ActivityResult verifyDocuments(LoanRequest request) {
        return safeExec("verifyDocuments", () ->
                (request.getEmail() != null && request.getSsn() != null)
                        ? success("Documents verified")
                        : failure("Missing identification docs")
        );
    }

    @Override
    @Transactional
    public ActivityResult assessRisk(LoanRequest request) {
        return safeExec("assessRisk", () ->
                request.getAmount().doubleValue() <= request.getAnnualIncome().doubleValue() * 5
                        ? success("Risk acceptable")
                        : failure("Loan too large relative to income")
        );
    }

    @Override
    @Transactional
    public ActivityResult approveLoan(LoanRequest request) {
        return safeExec("approveLoan", () -> success("Approval granted"));
    }

    @Override
    @Transactional
    public ActivityResult rejectLoan(String applicationId, String reason) {
        return safeExec("rejectLoan", () -> {
            return repo.findById(applicationId)
                    .map(app -> {
                        app.setStatus(LoanStatus.REJECTED);
                        repo.save(app);
                        return success("Rejected: " + reason);
                    })
                    .orElse(failure("App not found: " + applicationId));
        });
    }

    @Override
    @Transactional
    public ActivityResult sendNotification(String applicationId, String message, String email) {
        return safeExec("sendNotification", () -> {
            // mock email logic
            log.info("Notify {}: {}", email, message);
            return success("Notification sent");
        });
    }

    // --- Helper methods ---
    private ActivityResult safeExec(String stepName, Supplier<ActivityResult> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error("Activity {} failed", stepName, e);
            return ActivityResult.builder()
                    .success(false)
                    .message("Error in " + stepName + ": " + e.getMessage())
                    .stepName(stepName)
                    .errorCode("ERROR")
                    .build();
        }
    }

    private ActivityResult success(Object data) {
        return ActivityResult.builder().success(true).data(data).build();
    }

    private ActivityResult failure(String message) {
        return ActivityResult.builder().success(false).message(message).build();
    }
}
