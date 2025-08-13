package com.IntelStream.presentation.dto.response;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Jacksonized
public class AlertResponse {
    Long id;
    Long instrumentId;
    String instrumentSymbol;
    Long exchangeId;
    String exchangeCode;

    String alertType;      // e.g., PRICE_THRESHOLD, SIGNAL_TRIGGER, VOLUME_SPIKE
    String message;        // Alert description
    BigDecimal triggeredPrice;
    LocalDateTime triggeredAt;

    // Optional contextual fields
    String signal;         // BUY, SELL, HOLD
    Boolean acknowledged;
}
