package com.IntelStream.application.query.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class TopMoversQuery {

    @Builder.Default
    @Min(value = 1, message = "Limit must be at least 1")
    @Max(value = 100, message = "Limit cannot exceed 100")
    Integer limit = 10;

    @Builder.Default
    LocalDateTime since = LocalDateTime.now().minusHours(24);

    @Builder.Default
    @Pattern(regexp = "STOCK|CRYPTO|FOREX|COMMODITY", message = "Invalid instrument type")
    String instrumentType = null;

    @Builder.Default
    @Pattern(regexp = "GAINERS|LOSERS|VOLUME", message = "Invalid sort type")
    String sortType = "GAINERS";
}