package com.IntelStream.domain.event;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class InstrumentDeactivatedEvent {
    Long instrumentId;
    String symbol;
    LocalDateTime timestamp;
}
