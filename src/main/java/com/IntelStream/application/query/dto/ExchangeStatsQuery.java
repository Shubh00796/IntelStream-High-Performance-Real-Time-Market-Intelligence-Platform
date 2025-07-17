package com.IntelStream.application.query.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@Jacksonized
public class ExchangeStatsQuery {

    @NotNull(message = "Exchange ID is required")
    Long exchangeId;

    LocalDateTime from;
    LocalDateTime to;

    @Builder.Default
    Boolean includeInstrumentBreakdown = false;
}
