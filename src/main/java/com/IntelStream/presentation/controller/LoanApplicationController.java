package com.IntelStream.presentation.controller;


import com.IntelStream.application.workflow.LoanApplicationOrchestrator;
import com.IntelStream.infrastructure.persistence.entity.LoanApplication;
import com.IntelStream.infrastructure.persistence.enums.LoanStatus;
import com.IntelStream.infrastructure.persistence.repository.LoanApplicationRepository;
import com.IntelStream.presentation.dto.request.LoanRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Slf4j
public class LoanApplicationController {

    private final LoanApplicationOrchestrator orchestrator;
    private final LoanApplicationRepository repository;

    @PostMapping("/apply")
    public ResponseEntity<LoanApplication> applyForLoan(@RequestBody LoanRequest request) {
        log.info("📝 Received new loan application for {}", request.getApplicantName());
        LoanApplication application = orchestrator.orchestrateLoanApplication(request);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<LoanApplication> getApplicationById(@PathVariable String applicationId) {
        return repository.findByApplicationId(applicationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LoanApplication>> getApplicationsByStatus(@PathVariable LoanStatus status) {
        return ResponseEntity.ok(repository.findByStatus(status));
    }

    @GetMapping("/workflow/{workflowId}/status")
    public ResponseEntity<String> getWorkflowStatus(@PathVariable String workflowId) {
        String step = orchestrator.getWorkflowStatus(workflowId);
        return ResponseEntity.ok(step);
    }

    @PostMapping("/workflow/{workflowId}/cancel")
    public ResponseEntity<String> cancelWorkflow(@PathVariable String workflowId,
                                                 @RequestParam String reason) {
        orchestrator.cancelWorkflow(workflowId, reason);
        return ResponseEntity.ok("Workflow cancellation requested");
    }
}
