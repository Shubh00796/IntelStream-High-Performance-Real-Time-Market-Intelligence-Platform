package com.IntelStream.infrastructure.persistence.repository;


import com.IntelStream.infrastructure.persistence.entity.LoanApplication;
import com.IntelStream.infrastructure.persistence.enums.LoanStatus;
import com.IntelStream.infrastructure.persistence.enums.OrchestrationStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, String> {

    // 🔎 Derived Queries

    Optional<LoanApplication> findByApplicationId(String applicationId);

    Optional<LoanApplication> findByWorkflowId(String workflowId);

    List<LoanApplication> findByStatus(LoanStatus status);

    List<LoanApplication> findByCurrentStep(OrchestrationStep step);

    List<LoanApplication> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<LoanApplication> findByStatusAndCurrentStep(LoanStatus status, OrchestrationStep step);

    boolean existsByEmail(String email);

    long countByStatus(LoanStatus status);

    // 🔎 JPQL Custom Queries

    @Query("SELECT la FROM LoanApplication la WHERE la.status = :status AND la.updatedAt < :cutoff")
    List<LoanApplication> findStaleApplications(@Param("status") LoanStatus status, @Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT la FROM LoanApplication la WHERE la.rejectionReason IS NOT NULL AND la.status = 'REJECTED'")
    List<LoanApplication> findRejectedWithReason();

    @Query("SELECT la FROM LoanApplication la WHERE la.status = 'PENDING' AND la.createdAt < :cutoff")
    List<LoanApplication> findPendingOlderThan(@Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT COUNT(la) FROM LoanApplication la WHERE la.currentStep = :step AND la.status = 'PROCESSING'")
    long countProcessingByStep(@Param("step") OrchestrationStep step);
}
