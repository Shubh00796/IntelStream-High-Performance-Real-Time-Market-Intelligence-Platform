package com.IntelStream.domain.service.tradeevent;


import com.IntelStream.domain.model.TradeEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for analyzing trade events using Java 8 Streams.
 */
public interface TradeEventService {

    List<TradeEvent> filterBuyTrades(List<TradeEvent> events);

    List<TradeEvent> filterBySymbol(List<TradeEvent> events, String symbol);

    Map<String, Long> countByExchange(List<TradeEvent> events);

    BigDecimal totalVolumeBySymbol(List<TradeEvent> events, String symbol);

    Optional<TradeEvent> findHighestValueTrade(List<TradeEvent> events);

    Map<String, BigDecimal> averagePricePerSymbol(List<TradeEvent> events);

    Map<TradeEvent.TradeType, List<TradeEvent>> groupByTradeType(List<TradeEvent> events);

    List<TradeEvent> sortByTimestampDesc(List<TradeEvent> events);

    List<String> getDistinctSymbols(List<TradeEvent> events);

    List<TradeEvent> filterByTimeRange(List<TradeEvent> events, LocalDateTime from, LocalDateTime to);

    boolean allTradesAbovePrice(List<TradeEvent> events, BigDecimal price);

    long countSellTrades(List<TradeEvent> events);
}
