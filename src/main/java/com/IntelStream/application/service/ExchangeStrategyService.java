package com.IntelStream.application.service;

import com.IntelStream.application.strategy.ExchangeStrategy;
import com.IntelStream.domain.model.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeStrategyService {

    private final Map<String, ExchangeStrategy> strategies;

    public boolean isOpen(Exchange exchange, LocalDateTime now) {
        String code = exchange.getCode().toUpperCase();
        ExchangeStrategy strategy = strategies.get(code);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy for exchange: " + code);
        }
        return strategy.isMarketOpen(exchange, now);
    }
}
