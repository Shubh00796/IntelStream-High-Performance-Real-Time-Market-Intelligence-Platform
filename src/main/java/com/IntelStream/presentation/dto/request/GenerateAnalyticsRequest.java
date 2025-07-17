package com.IntelStream.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class GenerateAnalyticsRequest {

    @NotNull
    @Positive
    Long instrumentId;

    @NotNull
    LocalDateTime calculatedAt;

    @Builder.Default
    Integer movingAveragePeriod20 = 20;

    @Builder.Default
    Integer movingAveragePeriod50 = 50;

    @Builder.Default
    Integer rsiPeriod = 14;

    @Builder.Default
    Integer volatilityPeriod = 30;
}
