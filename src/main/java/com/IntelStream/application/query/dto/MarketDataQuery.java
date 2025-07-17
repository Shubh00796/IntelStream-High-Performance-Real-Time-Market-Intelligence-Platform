package com.IntelStream.application.query.dto;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class MarketDataQuery {

    @NotNull(message = "Instrument ID is required")
    @Positive(message = "Instrument ID must be positive")
    Long instrumentId;

    LocalDateTime startTime;
    LocalDateTime endTime;

    @Builder.Default
    @Min(value = 0, message = "Page must be non-negative")
    Integer page = 0;

    @Builder.Default
    @Min(value = 1, message = "Size must be at least 1")
    @Max(value = 1000, message = "Size cannot exceed 1000")
    Integer size = 20;

    @Builder.Default
    String sortBy = "timestamp";

    @Builder.Default
    @Pattern(regexp = "ASC|DESC", message = "Sort direction must be ASC or DESC")
    String sortDirection = "DESC";
}