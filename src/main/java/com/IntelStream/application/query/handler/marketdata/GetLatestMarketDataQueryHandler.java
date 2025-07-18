package com.IntelStream.application.query.handler.marketdata;


import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.infrastructure.persistence.mapper.MarketDataMapper;
import com.IntelStream.presentation.dto.response.MarketDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

// 3. Get Latest Tick for multiple instruments
@Component
@RequiredArgsConstructor
public class GetLatestMarketDataQueryHandler {
    private final MarketDataRepository marketDataRepository;
    private final MarketDataMapper marketDataMapper;

    public List<MarketDataResponse> handle(List<Long> instrumentIds) {
        return marketDataRepository.findLatestByInstrumentIds(instrumentIds)
                .stream()
                .map(marketDataMapper::toResponse)
                .toList();
    }
}
