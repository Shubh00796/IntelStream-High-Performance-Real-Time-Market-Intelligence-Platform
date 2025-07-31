package com.IntelStream.application.processor;

import com.IntelStream.domain.model.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
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


}
