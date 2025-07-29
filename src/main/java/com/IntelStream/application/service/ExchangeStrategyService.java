package com.IntelStream.application.service;

import com.IntelStream.application.strategy.ExchangeStrategy;
import com.IntelStream.domain.model.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeStrategyService {

    private final Map<String, ExchangeStrategy> strategies;

    public boolean isOpen(Exchange exchange, LocalDateTime now) {
        return getStrategyForExchange(exchange)
                .map(strategy -> strategy.isMarketOpen(exchange, now))
                .orElseThrow(() -> new IllegalArgumentException("No strategy for exchange: " + exchange.getCode()));
    }

    private Optional<ExchangeStrategy> getStrategyForExchange(Exchange exchange) {
        return Optional.ofNullable(strategies.get(normalizeCode(exchange.getCode())));
    }

    private String normalizeCode(String code) {
        return code == null ? "" : code.toUpperCase();
    }
}
