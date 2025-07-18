package com.IntelStream.application.query.handler.marketdata;


import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.infrastructure.persistence.mapper.MarketDataMapper;
import com.IntelStream.presentation.dto.response.MarketDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetExchangeMarketDataQueryHandler {
    private final MarketDataRepository marketDataRepository;
    private final MarketDataMapper marketDataMapper;

    public List<MarketDataResponse> handle(Long exchangeId, LocalDateTime since) {
        return marketDataRepository.findByExchangeIdAndTimestampAfter(exchangeId, since)
                .stream()
                .map(marketDataMapper::toResponse)
                .toList();
    }
}
