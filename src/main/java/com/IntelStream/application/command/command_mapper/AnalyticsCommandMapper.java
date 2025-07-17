package com.IntelStream.application.command.command_mapper;

import com.IntelStream.domain.model.AnalyticsSnapshot;
import com.IntelStream.domain.model.MarketData;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AnalyticsCommandMapper {

    /**
     * Maps calculated analytics values to a new AnalyticsSnapshot domain object.
     *
     * @param instrumentId       the ID of the instrument
     * @param marketDataList     the raw market data used for calculation (not used here but might be useful later)
     * @param calculatedAt       the timestamp when analytics were calculated
     * @param calculatedSnapshot the pre-computed analytics values
     * @return AnalyticsSnapshot domain model
     */
    public AnalyticsSnapshot toDomain(
            Long instrumentId,
            List<MarketData> marketDataList,
            LocalDateTime calculatedAt,
            AnalyticsSnapshot calculatedSnapshot
    ) {
        return AnalyticsSnapshot.builder()
                .instrumentId(instrumentId)
                .rsi(calculatedSnapshot.getRsi())
                .volatility(calculatedSnapshot.getVolatility())
                .movingAverage20(calculatedSnapshot.getMovingAverage20())
                .movingAverage50(calculatedSnapshot.getMovingAverage50())
                .volume24h(calculatedSnapshot.getVolume24h())
                .priceChange24h(calculatedSnapshot.getPriceChange24h())
                .priceChangePercent24h(calculatedSnapshot.getPriceChangePercent24h())
                .calculatedAt(calculatedAt)
                .build();
    }
}
