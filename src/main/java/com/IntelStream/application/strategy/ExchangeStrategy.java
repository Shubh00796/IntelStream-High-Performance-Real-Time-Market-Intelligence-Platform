package com.IntelStream.application.strategy;

import com.IntelStream.domain.model.Exchange;

import java.time.LocalDateTime;

public interface ExchangeStrategy {
    boolean isMarketOpen(Exchange exchange, LocalDateTime currentTime);
}
