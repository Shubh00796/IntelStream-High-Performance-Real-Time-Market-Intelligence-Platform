package com.IntelStream.application.query.handler;


import com.IntelStream.application.query.dto.MarketDataQuery;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.domain.service.market.MarketDataQueryService;
import com.IntelStream.infrastructure.persistence.mapper.MarketDataMapper;
import com.IntelStream.presentation.dto.response.MarketDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetMarketDataQueryHandler {

    private final MarketDataRepository marketDataRepository; // ✅ side-effect here
    private final MarketDataQueryService queryService;       // ✅ pure business logic
    private final MarketDataMapper marketDataMapper;         // ✅ also infra side-effect

    public List<MarketDataResponse> handle(MarketDataQuery query) {
        List<MarketData> rawData = marketDataRepository.findByInstrumentIdAndTimestampBetween(
                        query.getInstrumentId(), query.getStartTime(), query.getEndTime())
                .toList(); // because repo returns Stream

        List<MarketData> filtered = queryService.process(
                rawData,
                query.getSortDirection(),
                query.getPage(),
                query.getSize()
        );

        return filtered.stream()
                .map(marketDataMapper::toResponse)
                .toList();
    }
}
