package com.IntelStream.presentation.dto.request;

// File: presentation/dto/request/LoanRequest.java

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequest {
    private String applicantName;
    private String email;
    private BigDecimal amount;
    private String purpose;
    private String ssn;
    private BigDecimal annualIncome;
}

