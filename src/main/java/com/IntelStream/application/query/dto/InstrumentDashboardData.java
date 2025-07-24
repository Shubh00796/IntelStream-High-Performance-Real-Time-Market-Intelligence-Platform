package com.IntelStream.application.query.dto;

import com.IntelStream.domain.model.*;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class InstrumentDashboardData {
    Instrument instrument;
    MarketData latestPrice;
    AnalyticsSnapshot analyticsSnapshot;
    List<MarketData> priceHistory;
}