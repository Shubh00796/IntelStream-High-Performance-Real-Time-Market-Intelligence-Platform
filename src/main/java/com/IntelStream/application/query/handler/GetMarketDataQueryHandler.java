package com.IntelStream.application.query.handler;

import com.IntelStream.application.query.dto.MarketDataQuery;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.repository.MarketDataRepository;
import com.IntelStream.infrastructure.persistence.mapper.MarketDataMapper;
import com.IntelStream.presentation.dto.response.MarketDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class GetMarketDataQueryHandler {

    private final MarketDataRepository marketDataRepository;
    private final MarketDataMapper marketDataMapper;

    public List<MarketDataResponse> handle(MarketDataQuery query) {
        try (Stream<MarketData> stream = marketDataRepository
                .findByInstrumentIdAndTimestampBetween(
                        query.getInstrumentId(),
                        query.getStartTime(),
                        query.getEndTime())
        ) {
            return stream
                    .sorted(query.getSortDirection().equalsIgnoreCase("DESC")
                            ? Comparator.comparing(MarketData::getTimestamp).reversed()
                            : Comparator.comparing(MarketData::getTimestamp))
                    .map(marketDataMapper::toResponse)
                    .toList();
        }
    }
}
