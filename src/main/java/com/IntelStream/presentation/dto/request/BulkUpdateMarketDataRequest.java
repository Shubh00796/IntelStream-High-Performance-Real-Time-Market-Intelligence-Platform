package com.IntelStream.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class BulkUpdateMarketDataRequest {

    @NotEmpty
    @Valid
    List<UpdateMarketDataRequest> marketDataList;

    @NotBlank
    String batchId;

    @NotNull
    LocalDateTime processingTimestamp;
}
