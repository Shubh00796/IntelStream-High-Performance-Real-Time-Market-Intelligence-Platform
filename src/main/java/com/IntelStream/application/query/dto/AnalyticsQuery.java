package com.IntelStream.application.query.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
@Value
@Builder
@Jacksonized
public class AnalyticsQuery {

    @NotNull(message = "Instrument ID is required")
    @Positive(message = "Instrument ID must be positive")
    Long instrumentId;

    LocalDateTime startTime;
    LocalDateTime endTime;

    @Builder.Default
    Boolean includeOverbought = false;

    @Builder.Default
    Boolean includeOversold = false;

    @Builder.Default
    Integer limit = 100;
}
