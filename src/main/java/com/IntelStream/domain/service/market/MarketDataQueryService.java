package com.IntelStream.domain.service.market;

// application/query/service/MarketDataQueryService.java

import com.IntelStream.domain.model.MarketData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketDataQueryService {

    private final MarketDataQueryLogicService logicService;

    public List<MarketData> process(List<MarketData> rawData, String sortDirection, int page, int size) {
        return logicService.sortAndPaginate(rawData, sortDirection, page, size);
    }
}
