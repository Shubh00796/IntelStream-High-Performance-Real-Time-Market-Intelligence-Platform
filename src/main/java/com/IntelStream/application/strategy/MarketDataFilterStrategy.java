package com.IntelStream.application.strategy;


import com.IntelStream.domain.model.MarketData;

import java.util.List;

public interface MarketDataFilterStrategy {
    List<MarketData> filter(List<MarketData> data);
}
