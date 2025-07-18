package com.IntelStream.application.query.handler.marketdata;


import com.IntelStream.application.query.dto.TopMoversQuery;
import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.infrastructure.persistence.mapper.MarketDataMapper;
import com.IntelStream.presentation.dto.response.MarketDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetTopMoversQueryHandler {

    private final MarketDataRepository marketDataRepository;
    private final MarketDataMapper marketDataMapper;

    public List<MarketDataResponse> handle(TopMoversQuery topMoversQuery) {
        return marketDataRepository.findTopPriceMovements(topMoversQuery.getLimit(), topMoversQuery.getSince())
                .stream()
                .map(marketDataMapper::toResponse)
                .toList();
    }
}
