package com.IntelStream.application.query.handler.marketdata;


import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.infrastructure.persistence.mapper.MarketDataMapper;
import com.IntelStream.presentation.dto.response.MarketDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 2. Get Latest Tick for a single instrument
@Component
@RequiredArgsConstructor
public class GetLatestTickQueryHandler {
    private final MarketDataRepository marketDataRepository;
    private final MarketDataMapper marketDataMapper;

    public MarketDataResponse handle(Long instrumentId) {
        return marketDataRepository.findLatestByInstrumentId(instrumentId)
                .map(marketDataMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("No latest market data found for instrument: " + instrumentId));
    }
}
