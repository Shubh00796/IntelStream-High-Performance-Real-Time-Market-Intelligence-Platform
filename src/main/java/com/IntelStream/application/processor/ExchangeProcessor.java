package com.IntelStream.application.processor;

import com.IntelStream.domain.model.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExchangeProcessor {

    private final List<Exchange> exchanges;

    // Generic filter method
    private List<Exchange> filter(Predicate<Exchange> predicate) {
        return exchanges.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    // Generic groupBy method
    private <K> Map<K, List<Exchange>> groupBy(Function<Exchange, K> classifier) {
        return exchanges.stream()
                .collect(Collectors.groupingBy(classifier));
    }

    // Generic map method
    private <K, V> Map<K, V> toMap(Function<Exchange, K> keyMapper, Function<Exchange, V> valueMapper) {
        return exchanges.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    // Filters
    public List<Exchange> filterByActiveExchanges() {
        return filter(Exchange::isActive);
    }

    public List<Exchange> filterByInactiveExchanges() {
        return filter(exchange -> !exchange.isActive());
    }

    public List<Exchange> filterByCurrency(String currency) {
        return filter(exchange -> exchange.getCurrency().equalsIgnoreCase(currency));
    }

    public List<Exchange> filterByExchangeName(String name) {
        return filter(exchange -> exchange.getName().equalsIgnoreCase(name));
    }

    public List<Exchange> filterByMarketOpenBefore(LocalDateTime time) {
        return filter(exchange -> exchange.getMarketOpen().isBefore(time));
    }

    public List<Exchange> filterByMarketOpenAfter(LocalDateTime time) {
        return filter(exchange -> exchange.getMarketOpen().isAfter(time));
    }

    // Grouping
    public Map<String, List<Exchange>> groupByCurrency() {
        return groupBy(Exchange::getCurrency);
    }

    public Map<Boolean, List<Exchange>> groupByActiveStatus() {
        return groupBy(Exchange::isActive);
    }

    public Map<String, List<Exchange>> groupByExchangeNames() {
        return groupBy(Exchange::getName);
    }

    public Map<LocalDateTime, List<Exchange>> groupByMarketOpenDate() {
        return groupBy(Exchange::getMarketOpen);
    }

    // Mapping
    public Map<Long, String> getExchangeIdToCodeMap() {
        return toMap(Exchange::getId, Exchange::getCode);
    }

    public Map<Long, String> getExchangeIdToNameMap() {
        return toMap(Exchange::getId, Exchange::getName);
    }

    public Map<String, Exchange> getExchangeCodeToExchangeMap() {
        return toMap(Exchange::getCode, Function.identity());
    }

    public Map<String, Exchange> getExchangeNameToExchangeMap() {
        return toMap(Exchange::getName, Function.identity());
    }

    // Min/Max
    public Optional<Exchange> getEarliestMarketOpenExchange() {
        return exchanges.stream()
                .min(Comparator.comparing(Exchange::getMarketOpen));
    }

    public Optional<Exchange> getLatestMarketCloseExchange() {
        return exchanges.stream()
                .max(Comparator.comparing(Exchange::getMarketOpen));
    }

    // Count
    public long countActiveExchanges() {
        return exchanges.stream().filter(Exchange::isActive).count();
    }

    public long countExchangeByCurrency(String currency) {
        return exchanges.stream()
                .filter(exchange -> exchange.getCurrency().equalsIgnoreCase(currency))
                .count();
    }
}
