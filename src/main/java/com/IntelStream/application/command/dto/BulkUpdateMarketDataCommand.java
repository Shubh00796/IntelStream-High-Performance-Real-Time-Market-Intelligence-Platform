package com.IntelStream.application.command.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@Jacksonized
public class BulkUpdateMarketDataCommand {

    @NotEmpty(message = "Market data list cannot be empty")
    @Valid
    List<UpdateMarketDataCommand> marketDataList;

    @NotBlank(message = "Batch ID is required")
    String batchId;

    @NotNull(message = "Processing timestamp is required")
    LocalDateTime processingTimestamp;
}