package com.IntelStream.presentation.dto.request;


import com.IntelStream.infrastructure.persistence.enums.LoanStatus;
import com.IntelStream.infrastructure.persistence.enums.OrchestrationStep;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationResponse {
    private String applicationId;
    private String applicantName;
    private String email;
    private BigDecimal amount;
    private String purpose;
    private BigDecimal annualIncome;
    private LoanStatus status;
    private OrchestrationStep currentStep;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
