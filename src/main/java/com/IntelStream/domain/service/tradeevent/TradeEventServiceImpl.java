package com.IntelStream.domain.service.tradeevent;

import com.IntelStream.domain.model.TradeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TradeEventServiceImpl implements TradeEventService {
    @Override
    public List<TradeEvent> filterBuyTrades(List<TradeEvent> events) {
        return events.stream()
                .filter(TradeEvent::isBuy)
                .collect(Collectors.toList());
    }

    @Override
    public List<TradeEvent> filterBySymbol(List<TradeEvent> events, String symbol) {
        return events
                .stream()
                .filter(tradeEvent -> tradeEvent.getSymbol().equalsIgnoreCase(symbol))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> countByExchange(List<TradeEvent> events) {
        return events
                .stream()
                .collect(Collectors.groupingBy(TradeEvent::getExchange, Collectors.counting()));
    }

    @Override
    public BigDecimal totalVolumeBySymbol(List<TradeEvent> events, String symbol) {
        return events
                .stream()
                .filter(tradeEvent -> tradeEvent.getSymbol().equalsIgnoreCase(symbol))
                .map(TradeEvent::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Optional<TradeEvent> findHighestValueTrade(List<TradeEvent> events) {
        return events
                .stream()
                .max(Comparator.comparing(TradeEvent::getTradeValue));
    }

    @Override
    public Map<String, BigDecimal> averagePricePerSymbol(List<TradeEvent> events) {
        return events.stream()
                .collect(Collectors.groupingBy(
                        TradeEvent::getSymbol,
                        Collectors.collectingAndThen(
                                Collectors.mapping(TradeEvent::getPrice, Collectors.toList()),
                                prices -> {
                                    BigDecimal sum = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                                    return prices.isEmpty() ? BigDecimal.ZERO :
                                            sum.divide(BigDecimal.valueOf(prices.size()), 2, BigDecimal.ROUND_HALF_UP);
                                }
                        )
                ));
    }

    @Override
    public Map<TradeEvent.TradeType, List<TradeEvent>> groupByTradeType(List<TradeEvent> events) {
        return events
                .stream()
                .collect(Collectors.groupingBy(TradeEvent::getTradeType));
    }

    @Override
    public List<TradeEvent> sortByTimestampDesc(List<TradeEvent> events) {
        return events
                .stream()
                .sorted(Comparator.comparing(TradeEvent::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDistinctSymbols(List<TradeEvent> events) {
        return events
                .stream()
                .map(TradeEvent::getSymbol)
                .distinct()
                .toList();
    }

    @Override
    public List<TradeEvent> filterByTimeRange(List<TradeEvent> events, LocalDateTime from, LocalDateTime to) {
        return events
                .stream()
                .filter(tradeEvent -> !tradeEvent.getTimestamp().isBefore(from) && !tradeEvent.getTimestamp().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public boolean allTradesAbovePrice(List<TradeEvent> events, BigDecimal price) {
        return events
                .stream()
                .allMatch(tradeEvent -> tradeEvent.getPrice().compareTo(price) > 0);
    }

    @Override
    public long countSellTrades(List<TradeEvent> events) {
        return events.stream()
                .filter(TradeEvent::isSell)
                .count();
    }
}
