package com.IntelStream.infrastructure.persistence.entity;

// File: domain/model/LoanApplication.java

import com.IntelStream.infrastructure.persistence.enums.LoanStatus;
import com.IntelStream.infrastructure.persistence.enums.OrchestrationStep;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loan_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplication {

    @Id
    private String applicationId;

    private String applicantName;
    private String email;
    private BigDecimal amount;
    private String purpose;
    private String ssn;
    private BigDecimal annualIncome;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Enumerated(EnumType.STRING)
    private OrchestrationStep currentStep;

    private String rejectionReason;
    private String workflowId;
    private String runId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.applicationId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.status = LoanStatus.PENDING;
        this.currentStep = OrchestrationStep.CREDIT_CHECK;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
