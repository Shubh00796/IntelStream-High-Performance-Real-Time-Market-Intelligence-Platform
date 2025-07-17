package com.IntelStream.domain.event;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class InstrumentCreatedEvent {
    Long instrumentId;
    String symbol;
    String instrumentType;
    LocalDateTime timestamp;
}
