package com.IntelStream.application.query.handler.marketdata;


import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.infrastructure.persistence.mapper.MarketDataMapper;
import com.IntelStream.presentation.dto.response.MarketDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
// 5. Get Paged Market Data for an instrument
@Component
@RequiredArgsConstructor
public class GetPagedMarketDataQueryHandler {
    private final MarketDataRepository marketDataRepository;
    private final MarketDataMapper marketDataMapper;

    public List<MarketDataResponse> handle(Long instrumentId, int page, int size) {
        return marketDataRepository.findByInstrumentId(instrumentId, PageRequest.of(page, size))
                .stream()
                .map(marketDataMapper::toResponse)
                .toList();
    }
}

