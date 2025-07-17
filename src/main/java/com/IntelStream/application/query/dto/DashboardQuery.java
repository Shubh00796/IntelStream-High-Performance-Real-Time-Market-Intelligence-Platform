package com.IntelStream.application.query.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@Jacksonized
public class DashboardQuery {

    @NotEmpty(message = "Instrument IDs cannot be empty")
    List<@Positive(message = "Instrument ID must be positive") Long> instrumentIds;

    @Builder.Default
    Boolean includeAnalytics = true;

    @Builder.Default
    Boolean includeLatestPrice = true;

    @Builder.Default
    Boolean includePriceHistory = false;

    @Builder.Default
    Integer priceHistoryHours = 24;

    @Builder.Default
    LocalDateTime asOf = LocalDateTime.now();
}