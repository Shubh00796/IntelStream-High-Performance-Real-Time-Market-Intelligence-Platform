package com.IntelStream.application.processor;

import com.IntelStream.domain.model.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExchangeProcessor {

    private final List<Exchange> exchanges;

    // 1. Filter by Active Exchanges
    public List<Exchange> filterByActiveExchnages() {
        return exchanges
                .stream()
                .filter(Exchange::isActive)
                .collect(Collectors.toList());
    }

    //2. Filter by Inactive Exchanges
    public List<Exchange> filterByInactiveExchanges() {
        return exchanges
                .stream()
                .filter(exchange -> !exchange.isActive())
                .collect(Collectors.toList());
    }

    //3. Filter by Currency
    public List<Exchange> filterByCurrency(String currency) {
        return exchanges
                .stream()
                .filter(exchange -> exchange.getCurrency().equalsIgnoreCase(currency))
                .collect(Collectors.toList());
    }

    //4. Filter by Exchange Name
    public List<Exchange> filterByExchnageName(String name) {
        return exchanges
                .stream()
                .filter(exchange -> exchange.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    //5. Filter By MarketOpenBefore
    public List<Exchange> filterByMarketOpenBefore(LocalDateTime time) {
        return exchanges
                .stream()
                .filter(exchange -> exchange.getMarketOpen().isBefore(time))
                .collect(Collectors.toList());

    }

    //5. Filter By MarketOpenAfter
    public List<Exchange> filterByMarketOpenAfter(LocalDateTime time) {
        return exchanges
                .stream()
                .filter(exchange -> exchange.getMarketOpen().isAfter(time))
                .collect(Collectors.toList());
    }


    //6. Group By Currency
    public Map<String, List<Exchange>> groupByCurrency() {
        return exchanges
                .stream()
                .collect(Collectors.groupingBy(Exchange::getCurrency));
    }

    //7. Group By Active Status
    public Map<Boolean, List<Exchange>> groupByActiveStatus() {
        return exchanges
                .stream()
                .collect(Collectors.groupingBy(Exchange::isActive));
    }

    //8. Group By Exchange Name
    public Map<String, List<Exchange>> groupByExchangeNames() {
        return exchanges
                .stream()
                .collect(Collectors.groupingBy(Exchange::getName));
    }

    //9. Group By Market Open Date
    public Map<LocalDateTime, List<Exchange>> groupByMarketOpenDate() {
        return exchanges
                .stream()
                .collect(Collectors.groupingBy(Exchange::getMarketOpen));
    }

    //6. Map ExchangeId to Code
    public Map<Long, String> getExchangeIdToCodeMap() {
        return exchanges
                .stream()
                .collect(Collectors.toMap(Exchange::getId, Exchange::getCode));
    }

    //7. Map ExchangeId to Name
    public Map<Long, String> getExchangeIdtoNameMap() {
        return exchanges
                .stream()
                .collect(Collectors.toMap(Exchange::getId, Exchange::getName));
    }

    //8. Map ExchnageCode to full Exchange Object
    public Map<String, Exchange> getExchangeCodetoExchangeMap() {
        return exchanges.stream()
                .collect(Collectors.toMap(
                        Exchange::getCode,
                        Function.identity(
                        )));
    }

    //9. Map ExchangeName to full Exchange Object
    public Map<String, Exchange> getExchangeNametoExchnageMap() {
        return exchanges
                .stream()
                .collect(Collectors.toMap(Exchange::getName,
                        Function.identity()));
    }

    //10. Get Earliest Market Open Exchange
    public Optional<Exchange> getEarliestMarketOpenExchange() {
        return exchanges.stream()
                .min(Comparator.comparing(Exchange::getMarketOpen));
    }

    //11. Get Latest Market Open Exchange
    public Optional<Exchange> getLatestMarketCloseExchange() {
        return exchanges
                .stream()
                .max(Comparator.comparing(Exchange::getMarketOpen));
    }

    //12. Count Exchanges by Active Status
    public long countActiveExchanges() {
        return exchanges
                .stream()
                .filter(Exchange::isActive)
                .count();
    }

    //13. Count Exchanges by Currency
    public long countExchangeByCurrency(String currency) {
        return exchanges
                .stream()
                .filter(exchange -> exchange.getCurrency().equalsIgnoreCase(currency))
                .count();
    }


}
