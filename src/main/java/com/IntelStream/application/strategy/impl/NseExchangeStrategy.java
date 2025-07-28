package com.IntelStream.application.strategy.impl;

import com.IntelStream.application.strategy.ExchangeStrategy;
import com.IntelStream.domain.model.Exchange;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("NSE")
public class NseExchangeStrategy implements ExchangeStrategy {
    @Override
    public boolean isMarketOpen(Exchange exchange, LocalDateTime currentTime) {
        // Add holiday checks or Indian-specific logic
        return exchange.isMarketOpen(currentTime); // Could enrich further
    }
}
