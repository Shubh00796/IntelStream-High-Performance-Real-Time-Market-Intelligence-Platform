package com.IntelStream.application.processor;

import com.IntelStream.domain.model.MarketData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MarketDataProcessor {

    private final List<MarketData> marketDataList;


    // 1. Get top N instruments by volume
    public List<MarketData> getTopInstrumentsByVolume(int topN) {
        return marketDataList.stream()
                .sorted(Comparator.comparing(MarketData::getVolume).reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }

    // 1. Get top  instruments by price
    public List<MarketData> getTopInstrumentsByPrice(int topPrices) {
        return marketDataList.stream()
                .sorted(Comparator.comparing(MarketData::getPrice).reversed())
                .limit(topPrices)
                .collect(Collectors.toList());
    }


    // 2. Average price per instrument symbol
    public Map<String, BigDecimal> getAveragePricePerInstrument() {
        return marketDataList.stream()
                .collect(Collectors.groupingBy(
                        MarketData::getInstrumentSymbol,
                        Collectors.mapping(MarketData::getPrice,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        prices -> prices.stream()
                                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                                .divide(BigDecimal.valueOf(prices.size()), 2, BigDecimal.ROUND_HALF_UP)
                                )
                        )
                ));
    }


    // 3. Filter by exchange ID
    public List<MarketData> filterByExchange(Long exchangeId) {
        return marketDataList.stream()
                .filter(marketData -> Objects.equals(marketData.getExchangeId(), exchangeId))
                .collect(Collectors.toList());

    }

    // 4. Filter by instrument type
    public List<MarketData> filterByInstrumentType(String instrumentType) {
        return marketDataList.stream()
                .filter(marketData -> Objects.equals(marketData.getInstrumentSymbol(), instrumentType))
                .collect(Collectors.toList());
    }

    // 4. Filter by instrument type and exchange ID
    public List<MarketData> filterByInstrumentTypeAndExchange(String instrumentType, Long exchnageId) {
        return marketDataList.stream()
                .filter(marketData -> Objects.equals(marketData.getInstrumentSymbol(), instrumentType) &&
                        Objects.equals(marketData.getExchangeId(), exchnageId))
                .collect(Collectors.toList());
    }


}
