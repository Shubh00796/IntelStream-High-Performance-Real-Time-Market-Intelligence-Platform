package com.IntelStream.application.service;

import com.IntelStream.application.common.exception.ResourceNotFoundException;
import com.IntelStream.application.strategy.MarketDataFilterStrategy;
import com.IntelStream.domain.model.MarketData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MarketDataFilterService {

    private final Map<String, MarketDataFilterStrategy> strategies;

    public List<MarketData> applyFilter(String strategyKey, List<MarketData> data) {
        MarketDataFilterStrategy strategy = strategies.get(strategyKey);
        if (strategy == null) {
            throw new ResourceNotFoundException("Invalid filter strategy: " + strategyKey);
        }
        return strategy.filter(data);
    }
}
