package com.IntelStream.application.query.handler.marketdata;


import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.infrastructure.persistence.mapper.MarketDataMapper;
import com.IntelStream.presentation.dto.response.MarketDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 1. Get Market Data by ID
@Component
@RequiredArgsConstructor
public class GetMarketDataByIdQueryHandler {
    private final MarketDataRepository marketDataRepository;
    private final MarketDataMapper marketDataMapper;

    public MarketDataResponse handle(Long id) {
        return marketDataRepository.findById(id)
                .map(marketDataMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("MarketData not found for ID: " + id));
    }
}
