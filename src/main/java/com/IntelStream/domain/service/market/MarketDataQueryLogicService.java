package com.IntelStream.domain.service.market;


import com.IntelStream.domain.model.MarketData;

import java.util.Comparator;
import java.util.List;

public class MarketDataQueryLogicService {

    public List<MarketData> sortAndPaginate(List<MarketData> data, String sortDirection, int page, int size) {
        Comparator<MarketData> comparator = Comparator.comparing(MarketData::getTimestamp);
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }

        return data.stream()
                .sorted(comparator)
                .skip((long) page * size)
                .limit(size)
                .toList();
    }
}
