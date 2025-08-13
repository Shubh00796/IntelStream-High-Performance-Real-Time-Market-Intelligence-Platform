package com.IntelStream.domain.model;


import lombok.*;

import java.time.LocalDate;

/**
 * Represents a time-based macroeconomic indicator like GDP, inflation, etc.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EconomicIndicator {
    Long id;
    String country;
    IndicatorType type;
    LocalDate date;
    double value;

    public enum IndicatorType {
        GDP, INFLATION, UNEMPLOYMENT, INTEREST_RATE
    }

    public boolean isNegativeGrowth() {
        return type == IndicatorType.GDP && value < 0;
    }

    public boolean isHighInflation() {
        return type == IndicatorType.INFLATION && value > 6.0;
    }

    public boolean isInterestRateCut() {
        return type == IndicatorType.INTEREST_RATE && value < 2.0;
    }
}
