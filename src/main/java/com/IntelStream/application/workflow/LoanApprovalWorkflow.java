package com.IntelStream.application.workflow;


import com.IntelStream.domain.model.ActivityResult;
import com.IntelStream.infrastructure.persistence.enums.LoanStatus;
import com.IntelStream.presentation.dto.request.LoanRequest;
import io.temporal.workflow.*;

@WorkflowInterface
public interface LoanApprovalWorkflow {

    /**
     * Starts the loan application workflow
     */
    @WorkflowMethod
    ActivityResult processLoanApplication(LoanRequest request);

    /**
     * Allows external signal to cancel an ongoing workflow
     */
    @SignalMethod
    void requestCancellation(String reason);

    /**
     * Returns current orchestration step of the workflow
     */
    @QueryMethod
    String getCurrentStep();

    /**
     * Returns current loan application status
     */
    @QueryMethod
    LoanStatus getCurrentStatus();
}
