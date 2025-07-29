package com.IntelStream.application.service;

import com.IntelStream.application.common.exception.ResourceNotFoundException;
import com.IntelStream.application.strategy.MarketDataFilterStrategy;
import com.IntelStream.domain.model.MarketData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarketDataFilterService {

    private final Map<String, MarketDataFilterStrategy> strategies;

    public List<MarketData> applyFilter(String strategyKey, List<MarketData> data) {
        return getStrategy(strategyKey)
                .map(strategy -> strategy.filter(data))
                .orElseThrow(() -> new ResourceNotFoundException("Invalid filter strategy: " + strategyKey));
    }

    private Optional<MarketDataFilterStrategy> getStrategy(String key) {
        return Optional.ofNullable(strategies.get(normalizeKey(key)));
    }

    private String normalizeKey(String key) {
        return key == null ? "" : key.trim().toLowerCase();
    }
}
