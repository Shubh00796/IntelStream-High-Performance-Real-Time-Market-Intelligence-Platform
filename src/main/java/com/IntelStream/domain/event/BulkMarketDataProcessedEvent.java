package com.IntelStream.domain.event;


import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class BulkMarketDataProcessedEvent {

    String batchId;
    Integer recordCount;
    LocalDateTime processingTimestamp;

    // Optional extension fields for future scalability
    // List<Long> instrumentIds;
    // String sourceSystem;
}
