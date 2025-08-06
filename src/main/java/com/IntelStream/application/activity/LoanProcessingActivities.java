package com.IntelStream.application.activity;


import com.IntelStream.domain.model.ActivityResult;
import com.IntelStream.infrastructure.persistence.enums.LoanStatus;
import com.IntelStream.infrastructure.persistence.enums.OrchestrationStep;
import com.IntelStream.presentation.dto.request.LoanRequest;
import io.temporal.activity.*;

@ActivityInterface
public interface LoanProcessingActivities {

    @ActivityMethod
    ActivityResult performCreditCheck(LoanRequest request);

    @ActivityMethod
    ActivityResult verifyDocuments(LoanRequest request);

    @ActivityMethod
    ActivityResult assessRisk(LoanRequest request);

    @ActivityMethod
    ActivityResult approveLoan(LoanRequest request);

    @ActivityMethod
    ActivityResult persistApplication(LoanRequest request, String workflowId, String runId);

    @ActivityMethod
    ActivityResult updateApplicationStatus(String applicationId, LoanStatus status, OrchestrationStep step);

    @ActivityMethod
    ActivityResult rejectLoan(String applicationId, String reason);

    @ActivityMethod
    ActivityResult sendNotification(String applicationId, String message, String email);
}
